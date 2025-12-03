package com.etfinder.mvc.like.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.like.service.LikeService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api")
public class LikeController {
	
	private final LikeService likeService;
	private final UserService userService;

	public LikeController(LikeService likeService, UserService userService) {
		this.likeService = likeService;
		this.userService = userService;
	}

	// 1. 좋아요 누르기
	@PostMapping("/likes/{etfId}")
	public ResponseEntity<String> addLike(
			@PathVariable Long etfId,
			@AuthenticationPrincipal String providerId) {

		// ① 로그인 여부 확인
		if (providerId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("로그인이 필요합니다.");
		}

		// ② providerId -> userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("유저 정보를 찾을 수 없습니다.");
		}

		// ③ 좋아요 등록
		int res = likeService.addLike(user.getUserId(), etfId);
		if (res == 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("이미 좋아요한 ETF입니다.");
		}

		return ResponseEntity.status(HttpStatus.CREATED)
				.body("좋아요가 등록되었습니다.");
	}

	// 2. 로그인 유저의 좋아요 리스트 조회
	@GetMapping("/users/me/likes")
	public ResponseEntity<?> getLikedList(
			@AuthenticationPrincipal String providerId) {

		// ① 로그인 여부
		if (providerId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("로그인이 필요합니다.");
		}

		// ② providerId -> userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("유저 정보를 찾을 수 없습니다.");
		}

		// ③ 좋아요 리스트 조회
		List<EtfProduct> list = likeService.getLikedEtfs(user.getUserId());
		if (list.isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

		return ResponseEntity.ok(list);
	}

	// 3. 좋아요 해제
	@DeleteMapping("/likes/{etfId}")
	public ResponseEntity<?> deleteLike(
			@PathVariable Long etfId,
			@AuthenticationPrincipal String providerId) {

		// ① 로그인 여부
		if (providerId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("로그인이 필요합니다.");
		}

		// ② providerId -> userId 조회
		User user = userService.getUserByProviderId(providerId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("유저 정보를 찾을 수 없습니다.");
		}

		// ③ 좋아요 해제
		int res = likeService.deleteLike(user.getUserId(), etfId);
		if (res == 0) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("이미 좋아요가 해제된 상태입니다.");
		}

		return ResponseEntity.ok("좋아요 해제가 완료되었습니다.");
	}
}
