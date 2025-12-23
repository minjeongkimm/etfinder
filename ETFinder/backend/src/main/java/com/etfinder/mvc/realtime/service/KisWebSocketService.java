package com.etfinder.mvc.realtime.service;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.etfinder.mvc.realtime.cache.RealTimePriceCache;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KisWebSocketService {

	private final SimpMessagingTemplate messagingTemplate;
	private final RealTimePriceCache priceCache;

	// 생성자 주입
	public KisWebSocketService(
			SimpMessagingTemplate messagingTemplate,
			RealTimePriceCache priceCache) {
		this.messagingTemplate = messagingTemplate;
		this.priceCache = priceCache;
	}

	// 설정 파일에서 값 가져오기
	@Value("${kis.appkey}")
	private String appKey;

	@Value("${kis.appsecret}")
	private String appSecret;

	// 실전투자 REST API URL (키 발급용)
	private static final String REAL_REST_URL = "https://openapi.koreainvestment.com:9443";

	// 실전투자 웹소켓 URL
	private static final String REAL_WS_URL = "ws://ops.koreainvestment.com:21000";

	private String approvalKey; // 발급받은 키를 여기에 저장

	// 현재 연결된 세션 (volatile: heartbeat 스레드에서 안전하게 읽기)
	private volatile WebSocketSession currentSession;
	private volatile String currentSessionId;

	// 지금 구독 중인 종목 코드들을 기억하는 변수 (다중 구독 지원)
	private Set<String> subscribedCodes = new HashSet<>();

	// Send/Subscribe 동기화 lock (heartbeat, subscribe 등 동시 send 방지)
	private final Object sendLock = new Object();

	// Keepalive (하트비트) 관련 필드
	private ScheduledExecutorService heartbeatScheduler;
	private ScheduledFuture<?> heartbeatTask;

	// 재연결 관련 필드
	private final AtomicBoolean isReconnecting = new AtomicBoolean(false);
	private ScheduledFuture<?> reconnectTask;
	private int reconnectAttempts = 0;

	// 설정 주입
	@Value("${kis.websocket.keepalive.enabled:true}")
	private boolean keepaliveEnabled;

	@Value("${kis.websocket.keepalive.intervalSeconds:30}")
	private int keepaliveIntervalSeconds;

	@Value("${kis.websocket.reconnect.enabled:true}")
	private boolean reconnectEnabled;

	@Value("${kis.websocket.reconnect.maxAttempts:5}")
	private int maxReconnectAttempts;

	// 1. 서버 켜지면 자동으로 실행되는 메소드
	@PostConstruct
	public void init() {
		try {
			// (1) 웹소켓 접속키 발급받기
			this.approvalKey = getApprovalKeyFromKis();
			if (approvalKey != null && approvalKey.length() > 10) {
				String maskedKey = approvalKey.substring(0, 5) + "..."
						+ approvalKey.substring(approvalKey.length() - 5);
				log.info("️웹소켓 접속키 발급 완료: {}", maskedKey);
			} else {
				log.info("웹소켓 접속키 발급 완료: (길이가 너무 짧음)");
			}

		} catch (Exception e) {
			log.error("접속키 발급 실패! 설정 파일(appkey/secret) 확인.", e);
		}
	}

	// 접속키 발급 요청 로직
	private String getApprovalKeyFromKis() {
		RestTemplate restTemplate = new RestTemplate();
		String url = REAL_REST_URL + "/oauth2/Approval";

		// 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 바디 설정
		Map<String, String> body = new HashMap<>();
		body.put("grant_type", "client_credentials");
		body.put("appkey", appKey);
		body.put("secretkey", appSecret);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		// API 호출 (POST)
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

		// 응답 JSON 파싱해서 approval_key만 빼기
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());
			return root.path("approval_key").asText();
		} catch (Exception e) {
			throw new RuntimeException("JSON 파싱 실패", e);
		}
	}

	// 2. 웹소켓 연결 및 구독 (단일 종목 - ETF 상세 페이지용)
	// 기존 구독을 모두 해지하고 새 종목만 구독
	public void connectAndSubscribe(String newStockCode) {
		if (this.approvalKey == null)
			return;

		try {
			// 1. 이미 연결된 세션이 있다면?
			if (currentSession != null && currentSession.isOpen()) {

				// 1-1. 이미 이 종목만 구독 중이면 스킵
				if (subscribedCodes.size() == 1 && subscribedCodes.contains(newStockCode)) {
					log.info("✅ [이미 구독 중] 종목: {}", newStockCode);
					return;
				}

				// 1-2. 기존 구독 종목들 모두 해지
				for (String code : new HashSet<>(subscribedCodes)) {
					sendPacket(currentSession, code, "2"); // 2 = Unregister (구독 취소)
					log.info("🗑️ [구독 취소] 이전 종목 해지: {}", code);
				}
				subscribedCodes.clear();

				// 1-3. 새로운 종목 '구독' 요청
				sendPacket(currentSession, newStockCode, "1"); // 1 = Register (구독)
				subscribedCodes.add(newStockCode);
				log.info("♻️ [신규 구독] 새 종목 전환: {}", newStockCode);
				return;
			}

			// 2. 아예 연결이 없으면 새로 연결
			StandardWebSocketClient client = new StandardWebSocketClient();
			WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
			KisHandler handler = new KisHandler(newStockCode);
			client.execute(handler, headers, URI.create(REAL_WS_URL)).get();

		} catch (Exception e) {
			log.error("❌ 연결/구독 에러", e);
		}
	}

	// 3. 다중 종목 구독 (모의투자 대시보드용)
	public void connectAndSubscribeMultiple(List<String> stockCodes) {
		if (this.approvalKey == null || stockCodes == null || stockCodes.isEmpty())
			return;

		try {
			// 1. 이미 연결된 세션이 있다면?
			if (currentSession != null && currentSession.isOpen()) {
				// 각 종목에 대해 구독 요청
				for (String code : stockCodes) {
					if (!subscribedCodes.contains(code)) {
						sendPacket(currentSession, code, "1"); // 1 = Register (구독)
						subscribedCodes.add(code);
						log.info("➕ [다중 구독] 종목 추가: {}", code);
					}
				}
				return;
			}

			// 2. 아예 연결이 없으면 새로 연결 (첫 번째 종목으로 초기화)
			StandardWebSocketClient client = new StandardWebSocketClient();
			WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
			KisHandler handler = new KisHandler(stockCodes);
			client.execute(handler, headers, URI.create(REAL_WS_URL)).get();

		} catch (Exception e) {
			log.error("❌ 다중 구독 에러", e);
		}
	}

	public void stopSubscribe() {
		try {
			if (currentSession != null && currentSession.isOpen() && !subscribedCodes.isEmpty()) {
				// 모든 구독 종목 해지
				for (String code : subscribedCodes) {
					sendPacket(currentSession, code, "2"); // "2" = 구독 취소 (Unregister)
					log.info("🛑 [구독 중단] 종목: {}", code);
				}
				// 변수 초기화
				subscribedCodes.clear();
			}
		} catch (Exception e) {
			log.error("구독 중단 요청 실패", e);
		}
	}

	// ==========================================
	// Send/Close 유틸리티 메서드
	// ==========================================

	/**
	 * Thread-safe send (sendLock 사용)
	 * 
	 * @return 전송 성공 여부
	 */
	private boolean safeSend(WebSocketSession session, String message) {
		if (session == null || !session.isOpen()) {
			return false;
		}

		synchronized (sendLock) {
			try {
				session.sendMessage(new TextMessage(message));
				return true;
			} catch (Exception e) {
				log.error("❌ [safeSend] 전송 실패 (sessionId: {})", session.getId(), e);
				return false;
			}
		}
	}

	/**
	 * 안전한 세션 종료 (이미 닫힌 경우 무시)
	 */
	private void safeClose(WebSocketSession session, String reason) {
		if (session == null)
			return;

		try {
			if (session.isOpen()) {
				session.close();
				log.info("🔒 [safeClose] 세션 종료 (ID: {}, 사유: {})", session.getId(), reason);
			}
		} catch (Exception e) {
			log.warn("⚠️ [safeClose] 종료 실패 (ID: {})", session.getId(), e);
		}
	}

	// ==========================================
	// Keepalive (하트비트) 관련 메서드
	// ==========================================

	/**
	 * 하트비트 시작 (연결 확립 시 호출)
	 */
	private void startHeartbeat(WebSocketSession session) {
		if (!keepaliveEnabled) {
			log.debug("⏭️ [Keepalive] 비활성화 (kis.websocket.keepalive.enabled=false)");
			return;
		}

		// 중복 스케줄링 방지
		stopHeartbeat();

		if (heartbeatScheduler == null || heartbeatScheduler.isShutdown()) {
			heartbeatScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
				Thread t = new Thread(r, "KIS-Heartbeat");
				t.setDaemon(true);
				return t;
			});
		}

		heartbeatTask = heartbeatScheduler.scheduleAtFixedRate(() -> {
			// ✅ 최신 currentSession 참조 (volatile 읽기)
			WebSocketSession current = currentSession;

			if (current == null || !current.isOpen()) {
				log.debug("⏭️ [Heartbeat] 세션 닫힘 또는 null, 중지");
				stopHeartbeat();
				return;
			}

			// Thread-safe send
			boolean sent = safeSend(current, "PING");
			if (sent) {
				log.debug("💓 [Heartbeat] PING 전송 (sessionId: {})", current.getId());
			} else {
				log.error("❌ [Heartbeat] 전송 실패, 중지");
				stopHeartbeat();
			}
		}, 0, keepaliveIntervalSeconds, TimeUnit.SECONDS);

		log.info("✅ [Keepalive] 시작 ({}초 간격)", keepaliveIntervalSeconds);
	}

	/**
	 * 하트비트 중지 (연결 종료 시 호출)
	 */
	private void stopHeartbeat() {
		if (heartbeatTask != null && !heartbeatTask.isCancelled()) {
			heartbeatTask.cancel(false);
			log.debug("🛑 [Heartbeat] 중지됨");
		}
	}

	// ==========================================
	// 재연결 관련 메서드
	// ==========================================

	/**
	 * 재연결 스케줄링 (exponential backoff)
	 */
	private void scheduleReconnect() {
		if (!reconnectEnabled) {
			log.debug("⏭️ [재연결] 비활성화 (kis.websocket.reconnect.enabled=false)");
			return;
		}

		if (reconnectAttempts >= maxReconnectAttempts) {
			log.error("❌ [재연결] 최대 시도 횟수 초과 ({}/{})", reconnectAttempts, maxReconnectAttempts);
			reconnectAttempts = 0; // 리셋
			return;
		}

		// 중복 재연결 방지
		if (!isReconnecting.compareAndSet(false, true)) {
			log.warn("⚠️ [재연결] 이미 재연결 진행 중");
			return;
		}

		long delaySeconds = (long) Math.pow(2, reconnectAttempts); // 1, 2, 4, 8, 16...
		reconnectAttempts++;

		log.info("🔄 [재연결] {}초 후 재연결 시도 ({}/{})",
				delaySeconds, reconnectAttempts, maxReconnectAttempts);

		// 재연결 스케줄링
		if (heartbeatScheduler != null && !heartbeatScheduler.isShutdown()) {
			heartbeatScheduler.schedule(() -> {
				reconnectWithPreviousSubscriptions();
				isReconnecting.set(false);
			}, delaySeconds, TimeUnit.SECONDS);
		}
	}

	/**
	 * 이전 구독 종목들로 재연결
	 */
	private void reconnectWithPreviousSubscriptions() {
		try {
			// 세션이 이미 열려있으면 재연결 불필요
			if (currentSession != null && currentSession.isOpen()) {
				log.info("✅ [재연결] 세션이 이미 열려있음 - 재연결 취소");
				reconnectAttempts = 0;
				return;
			}

			// 이전 구독 종목 저장
			Set<String> previousCodes = new HashSet<>(subscribedCodes);

			if (previousCodes.isEmpty()) {
				log.info("📭 [재연결] 구독 종목이 없어 재연결하지 않음");
				return;
			}

			log.info("🔄 [재연결] 시도 중... 이전 구독: {}", previousCodes);

			// 재연결
			connectAndSubscribeMultiple(List.copyOf(previousCodes));

			log.info("✅ [재연결] 성공");
			reconnectAttempts = 0; // 성공 시 카운터 리셋

		} catch (Exception e) {
			log.error("❌ [재연결] 실패", e);
			// 재연결 실패 시 다시 스케줄링
			scheduleReconnect();
		}
	}

	// 패킷 전송 도우미 함수
	private void sendPacket(WebSocketSession session, String code, String trType) throws Exception {
		Map<String, String> header = new HashMap<>();
		header.put("approval_key", approvalKey);
		header.put("custtype", "P");
		header.put("tr_type", trType); // tr_type: 1=구독, 2=해지
		header.put("content-type", "utf-8");

		Map<String, String> input = new HashMap<>();
		input.put("tr_id", "H0STCNT0");
		input.put("tr_key", code);

		Map<String, Object> body = new HashMap<>();
		body.put("input", input);

		Map<String, Object> requestMap = new HashMap<>();
		requestMap.put("header", header);
		requestMap.put("body", body);

		String requestJson = new ObjectMapper().writeValueAsString(requestMap);
		session.sendMessage(new TextMessage(requestJson));
	}

	// 내부 핸들러
	private class KisHandler extends TextWebSocketHandler {

		// 초기 연결 시 구독할 종목들
		private final List<String> initStockCodes;

		// 단일 종목 생성자 (기존 호환성)
		public KisHandler(String initStockCode) {
			this.initStockCodes = List.of(initStockCode);
		}

		// 다중 종목 생성자
		public KisHandler(List<String> initStockCodes) {
			this.initStockCodes = initStockCodes;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			currentSession = session;
			log.info("✅ 한투 소켓 연결됨 (세션 ID: {})", session.getId());

			// 연결되자마자 초기 종목들 구독
			for (String code : initStockCodes) {
				sendPacket(session, code, "1");
				subscribedCodes.add(code);
				log.info("📡 [초기 구독] 종목: {}", code);
			}

			// Keepalive 시작
			startHeartbeat(session);

			// 재연결 카운터 리셋
			reconnectAttempts = 0;
		}

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) {
			String msg = message.getPayload();

			// 📊 진단: PING/제어 메시지 상세 로깅
			if (msg.contains("PING")) {
				String preview = msg.length() > 100 ? msg.substring(0, 100) + "..." : msg;
				log.debug("💓 [PING 수신] 길이: {}, 내용: {}", msg.length(), preview);
				// TODO: PING 응답 구현 필요 (단계 2)
				return;
			}

			if (msg.startsWith("0") || msg.startsWith("1")) {
				try {
					String[] parts = msg.split("\\|");
					if (parts.length > 3) {
						String dataPart = parts[3];
						String[] details = dataPart.split("\\^");

						// 종목코드를 메시지에서 직접 추출
						String receivedCode = details[0]; // 첫번째가 종목코드
						String currentPriceStr = details[2]; // 세번째가 현재가

						// 구독 중인 종목 데이터만 프론트로 전송
						if (subscribedCodes.contains(receivedCode)) {
							// 1️⃣ 기존 로직: 프론트엔드로 STOMP 전송
							messagingTemplate.convertAndSend("/topic/price/" + receivedCode, currentPriceStr);

							// 2️⃣ 신규 로직: 서버 캐시 업데이트
							try {
								Integer price = Integer.parseInt(currentPriceStr);
								priceCache.updatePrice(receivedCode, price);
							} catch (NumberFormatException e) {
								log.warn("⚠️ [가격 파싱 실패] 종목: {}, 가격: {}", receivedCode, currentPriceStr);
							}

							// log.info("💸 [시세 수신] {}: {}원", receivedCode, currentPrice);
						}
					}
				} catch (Exception e) {
					log.error("❌ 파싱 에러", e);
				}
			} else {
				// 시스템 메시지 (구독 성공 등)
				// log.info("ℹ️ 시스템: {}", msg);
			}
		}

		// 연결 종료 시 상세 진단 정보 로깅
		@Override
		public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status)
				throws Exception {
			// 📊 상세 진단 로그
			log.warn("🔌 [연결 종료] 세션 ID: {}", session.getId());
			log.warn("  └─ 종료 코드: {} ({})", status.getCode(), getCloseCodeDescription(status.getCode()));
			log.warn("  └─ 종료 사유: {}", status.getReason() != null && !status.getReason().isEmpty()
					? status.getReason()
					: "(사유 없음)");
			log.warn("  └─ 세션 상태: isOpen={}", session.isOpen());
			log.warn("  └─ 구독 종목 수: {}", subscribedCodes.size());
			if (!subscribedCodes.isEmpty()) {
				log.warn("  └─ 구독 종목: {}", subscribedCodes);
			}

			currentSession = null;

			// Keepalive 중지
			stopHeartbeat();

			// 재연결 스케줄링
			scheduleReconnect();
		}

		// 전송 에러 시 상세 스택 로깅
		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			log.error("❌ [전송 에러] 세션 ID: {}", session.getId());
			log.error("  └─ 예외 타입: {}", exception.getClass().getName());
			log.error("  └─ 메시지: {}", exception.getMessage());
			log.error("  └─ 스택 트레이스:", exception);
		}

		// CloseStatus 코드 설명 헬퍼
		private String getCloseCodeDescription(int code) {
			switch (code) {
				case 1000:
					return "정상 종료";
				case 1001:
					return "엔드포인트 종료";
				case 1002:
					return "프로토콜 에러";
				case 1003:
					return "지원하지 않는 데이터";
				case 1006:
					return "비정상 종료 (연결 끊김)";
				case 1007:
					return "잘못된 페이로드";
				case 1008:
					return "정책 위반";
				case 1009:
					return "메시지 너무 큼";
				case 1011:
					return "서버 에러";
				default:
					return "알 수 없음";
			}
		}
	}
}