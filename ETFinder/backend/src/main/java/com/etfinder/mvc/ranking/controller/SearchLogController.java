package com.etfinder.mvc.ranking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.ranking.dto.SearchLog;
import com.etfinder.mvc.ranking.service.SearchLogService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/search/history")
public class SearchLogController {

	private final SearchLogService searchLogService;
	private final UserService userService;

	public SearchLogController(SearchLogService searchLogService, UserService userService) {
		this.searchLogService = searchLogService;
		this.userService = userService;
	}

	// 1. 사용자별 검색 로그 조회
	@GetMapping
	public ResponseEntity<?> getSearchLog(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

		// 2) providerId로 실제 userId 찾기
		User user = userService.getUserByProviderId(providerId);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보를 찾을 수 없습니다.");

		List<SearchLog> list = searchLogService.selectSearchLogByUser(user.getUserId());

		// 비어 있어도 200 + [] 반환
		return ResponseEntity.ok(list);
	}

	// 2. 사용자별 특정 로그 삭제 (logId 기준)
	@DeleteMapping("/{searchLogId}")
	public ResponseEntity<?> deleteSearchLogByLogId(@AuthenticationPrincipal String providerId,
			@PathVariable("searchLogId") Long searchLogId) {

		// 1) 로그인 여부 확인
		if (providerId == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

		// 2) providerId로 실제 userId 찾기
		User user = userService.getUserByProviderId(providerId);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보를 찾을 수 없습니다.");

		// 3) 특정 로그 삭제
		int res = searchLogService.deleteSearchLogById(user.getUserId(), searchLogId);

		// 삭제할 대상이 없으면 404
		if (res == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 검색 기록을 찾을 수 없습니다.");
		}

		// 성공 시 바디 없이 204
		return ResponseEntity.noContent().build();

	}

	// 3. 사용자별 검색 로그 전체 삭제
	@DeleteMapping
	public ResponseEntity<?> deleteAllSearchLog(@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");

		// 2) providerId로 실제 userId 찾기
		User user = userService.getUserByProviderId(providerId);
		if (user == null)
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유저 정보를 찾을 수 없습니다.");

		// 3) 전체 로그 삭제
		// res 값이 0이어도 "이미 비어있음" 상태이므로 에러로 보지 않음
		searchLogService.deleteAllSearchLog(user.getUserId());

		// 항상 204 No Content
		return ResponseEntity.noContent().build();

	}

}
