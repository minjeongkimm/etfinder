package com.etfinder.mvc.realtime.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.realtime.service.KisWebSocketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/realtime")
@RequiredArgsConstructor
public class RealtimeController {

	private final KisWebSocketService kisWebSocketService;

	// 연결 요청
	@GetMapping("/connect")
	public ResponseEntity<String> connect(@RequestParam String code) {
		kisWebSocketService.connectAndSubscribe(code);

		return ResponseEntity.ok().body("✅ [" + code + "] 실시간 시세 연결 요청 성공!");
	}

	// 연결 해제 요청
	@GetMapping("/disconnect")
	public ResponseEntity<String> disconnect() {
		kisWebSocketService.stopSubscribe();

		return ResponseEntity.ok().body("🛑 실시간 시세 구독 중단 완료!");
	}

	// 다중 종목 연결 요청 (모의투자 대시보드용)
	@PostMapping("/connect-multiple")
	public ResponseEntity<String> connectMultiple(@RequestBody List<String> codes) {
		if (codes == null || codes.isEmpty()) {
			return ResponseEntity.badRequest().body("❌ 종목 코드 목록이 비어있습니다.");
		}

		kisWebSocketService.connectAndSubscribeMultiple(codes);

		return ResponseEntity.ok().body("✅ [" + codes.size() + "개 종목] 실시간 시세 연결 요청 성공!");
	}
}