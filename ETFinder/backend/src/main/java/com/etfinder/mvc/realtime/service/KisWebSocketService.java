package com.etfinder.mvc.realtime.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KisWebSocketService {

	private final SimpMessagingTemplate messagingTemplate;

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

	// 현재 연결된 세션을 저장해두는 변수
	private WebSocketSession currentSession;

	// 지금 구독 중인 종목 코드를 기억하는 변수
	private String currentSubscribedCode = null;

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

	// 2. 웹소켓 연결 및 구독
	public void connectAndSubscribe(String newStockCode) {
		if (this.approvalKey == null)
			return;

		try {
			// 1. 이미 연결된 세션이 있다면?
			if (currentSession != null && currentSession.isOpen()) {

				// 1-1. 기존에 보고 있던 종목이 있으면 '구독 취소' 요청
				if (currentSubscribedCode != null && !currentSubscribedCode.equals(newStockCode)) {
					sendPacket(currentSession, currentSubscribedCode, "2"); // 2 = Unregister (구독 취소)
					log.info("🗑️ [구독 취소] 이전 종목 해지: {}", currentSubscribedCode);
				}

				// 1-2. 새로운 종목 '구독' 요청
				sendPacket(currentSession, newStockCode, "1"); // 1 = Register (구독)
				currentSubscribedCode = newStockCode; // 현재 종목 업데이트
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

	public void stopSubscribe() {
		try {
			if (currentSession != null && currentSession.isOpen() && currentSubscribedCode != null) {
				// "2" = 구독 취소 (Unregister)
				sendPacket(currentSession, currentSubscribedCode, "2");
				log.info("🛑 [구독 중단] 상세 페이지 이탈 -> 수신 종료: {}", currentSubscribedCode);

				// 변수 초기화
				currentSubscribedCode = null;
			}
		} catch (Exception e) {
			log.error("구독 중단 요청 실패", e);
		}
	}

	// 패킷 전송 도우미 함수
	private void sendPacket(WebSocketSession session, String code, String trType) throws Exception {
		Map<String, String> header = new HashMap<>();
		header.put("approval_key", approvalKey);
		header.put("custtype", "P");
		header.put("tr_type", trType);  // tr_type: 1=구독, 2=해지
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

		// 초기 연결 시 구독할 종목
		private final String initStockCode;

		public KisHandler(String initStockCode) {
			this.initStockCode = initStockCode;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			currentSession = session;
			log.info("✅ 한투 소켓 연결됨");

			// 연결되자마자 초기 종목 구독
			sendPacket(session, initStockCode, "1");
			currentSubscribedCode = initStockCode;
		}

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) {
			String msg = message.getPayload();

			if (msg.contains("PING"))
				return;

			if (msg.startsWith("0") || msg.startsWith("1")) {
				try {
					String[] parts = msg.split("\\|");
					if (parts.length > 3) {
						String dataPart = parts[3];
						String[] details = dataPart.split("\\^");

						// 종목코드를 메시지에서 직접 추출
						String receivedCode = details[0]; // 첫번째가 종목코드
						String currentPrice = details[2]; // 세번째가 현재가

						// 현재 구독 중인 종목 데이터만 프론트로 전송
						if (receivedCode.equals(currentSubscribedCode)) {
							messagingTemplate.convertAndSend("/topic/price/" + receivedCode, currentPrice);
//                            log.info("💸 [시세 수신] {}: {}원", receivedCode, currentPrice);
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

		// 연결 끊기면 변수 초기화
		@Override
		public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status)
				throws Exception {
			log.info("🔌 한투 웹소켓 연결 종료");
			currentSession = null;
		}
	}
}