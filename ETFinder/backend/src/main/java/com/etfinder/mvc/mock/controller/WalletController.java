package com.etfinder.mvc.mock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.mock.dto.DashboardResponse;
import com.etfinder.mvc.mock.dto.MinuteAssetPoint;
import com.etfinder.mvc.mock.dto.MockHoldingResponse;
import com.etfinder.mvc.mock.dto.MockRankingResponse;
import com.etfinder.mvc.mock.dto.WalletResponse;
import com.etfinder.mvc.mock.service.MockRankingService;
import com.etfinder.mvc.mock.service.WalletService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

	private final WalletService walletService;
	private final MockRankingService rankingService;
	private final UserService userService;

	public WalletController(
			WalletService walletService,
			MockRankingService rankingService,
			UserService userService) {
		this.walletService = walletService;
		this.rankingService = rankingService;
		this.userService = userService;
	}

	/**
	 * 1. 가상 지갑 생성 (또는 조회)
	 * POST /api/wallet
	 */
	@PostMapping
	public ResponseEntity<?> createWallet(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 지갑 생성 또는 조회 (getWalletSummary에서 없으면 자동 생성)
		WalletResponse wallet = walletService.getWalletSummary(user.getUserId());

		return new ResponseEntity<>(wallet, HttpStatus.CREATED);
	}

	/**
	 * 2. 지갑 요약 정보 조회
	 * GET /api/wallet
	 */
	@GetMapping
	public ResponseEntity<?> getWalletSummary(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 총자산 갱신
		walletService.refreshTotalAsset(user.getUserId());

		// 4) 지갑 조회 (총자산 갱신 후)
		WalletResponse wallet = walletService.getWalletSummary(user.getUserId());

		return new ResponseEntity<>(wallet, HttpStatus.OK);
	}

	/**
	 * 3. 지갑 초기화 (보유 종목, 거래 내역 모두 삭제 후 초기 상태로)
	 * DELETE /api/wallet
	 */
	@DeleteMapping
	public ResponseEntity<?> resetWallet(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 지갑 리셋
		int result = walletService.resetWallet(user.getUserId());

		if (result > 0) {
			return ResponseEntity.ok("지갑이 초기화되었습니다.");
		}

		return new ResponseEntity<>("지갑 초기화에 실패했습니다.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * 4. 보유 종목 조회
	 * GET /api/wallet/holdings
	 */
	@GetMapping("/holdings")
	public ResponseEntity<?> getHoldings(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 보유 종목 조회 (MockHoldingMapper.selectHoldingView)
		// 이 메서드는 이미 etf_product와 조인하여 평가금, 손익률 등을 계산함
		List<MockHoldingResponse> holdings = walletService.getDashboard(user.getUserId()).getHoldings();

		if (holdings == null || holdings.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(holdings, HttpStatus.OK);
	}

	/**
	 * 5. 대시보드 통합 정보 조회
	 * GET /api/wallet/dashboard
	 */
	@GetMapping("/dashboard")
	public ResponseEntity<?> getDashboard(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 총자산 갱신
		walletService.refreshTotalAsset(user.getUserId());

		// 4) 대시보드 정보 조회
		DashboardResponse dashboard = walletService.getDashboard(user.getUserId());

		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	/**
	 * 6. 모의투자 랭킹 조회
	 * GET /api/wallet/rankings
	 */
	@GetMapping("/rankings")
	public ResponseEntity<?> getRankings(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 랭킹 정보 조회
		MockRankingResponse ranking = rankingService.getRankingInfo(user.getUserId());

		return new ResponseEntity<>(ranking, HttpStatus.OK);
	}

	/**
	 * 7. 분 단위 자산 추이 조회 (차트용)
	 * GET /api/wallet/asset-trend/minute?minutes=60
	 * 
	 * @param minutes 조회 범위 (기본 60분)
	 */
	@GetMapping("/asset-trend/minute")
	public ResponseEntity<?> getMinuteTrend(
			@AuthenticationPrincipal String providerId,
			@org.springframework.web.bind.annotation.RequestParam(defaultValue = "60") int minutes) {

		// 1) 로그인 여부 확인
		if (providerId == null) {
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
		}

		// 2) providerId → userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
		}

		// 3) 분 단위 추이 조회
		List<MinuteAssetPoint> points = walletService.getMinuteTrend(user.getUserId(), minutes);

		// 4) 응답 구성
		java.util.Map<String, Object> response = new java.util.HashMap<>();
		response.put("points", points);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
