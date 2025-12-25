package com.etfinder.mvc.mock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.mock.dto.TradeHistoryResponse;
import com.etfinder.mvc.mock.dto.TradeRequest;
import com.etfinder.mvc.mock.service.TradeService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

	private final TradeService tradeService;
	private final UserService userService;

	public TradeController(TradeService tradeService, UserService userService) {
		this.tradeService = tradeService;
		this.userService = userService;
	}

	/**
	 * 1. 매수/매도 거래 실행
	 * POST /api/trades
	 */
	@PostMapping
	public ResponseEntity<?> executeTrade(
			@AuthenticationPrincipal String providerId,
			@RequestBody TradeRequest request) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		try {
			// 3) 거래 실행
			int result = tradeService.executeTrade(user.getUserId(), request);

			if (result > 0) {
				String tradeType = "BUY".equals(request.getTradeType().toUpperCase()) ? "매수" : "매도";
				return new ResponseEntity<>(tradeType + " 거래가 완료되었습니다.", HttpStatus.CREATED);
			}

			return new ResponseEntity<>("거래 처리에 실패했습니다.", HttpStatus.BAD_REQUEST);

		} catch (IllegalArgumentException e) {
			// 유효성 검사 실패 (잘못된 요청)
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			// 비즈니스 로직 실패 (잔액 부족, 수량 부족 등)
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			// 기타 예외
			return new ResponseEntity<>("거래 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 2. 최근 거래 내역 조회
	 * GET /api/trades
	 */
	@GetMapping
	public ResponseEntity<?> getRecentTrades(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 최근 거래 내역 조회
		List<TradeHistoryResponse> trades = tradeService.getRecentTrades(user.getUserId());

		if (trades == null || trades.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(trades, HttpStatus.OK);
	}
}

