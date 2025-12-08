package com.etfinder.mvc.etf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;
import com.etfinder.mvc.etf.service.EtfSearchService;
import com.etfinder.mvc.ranking.dto.SearchLog;
import com.etfinder.mvc.ranking.service.SearchLogService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/etfs")
public class EtfSearchController {

	private final EtfSearchService etfSearchService;
	private final SearchLogService searchLogService;
	private final UserService userService;

	public EtfSearchController(EtfSearchService etfSearchService, SearchLogService searchLogService,
			UserService userService) {
		this.etfSearchService = etfSearchService;
		this.searchLogService = searchLogService;
		this.userService = userService;
	}

	// 1. 전체 조회
	@GetMapping
	public ResponseEntity<?> list() {
		List<EtfProduct> list = etfSearchService.selectAllEtf();

		if (list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<EtfProduct>>(list, HttpStatus.OK);

	}

	// 2. 상세 조회
	@GetMapping("/{etfId}")
	public ResponseEntity<?> detail(@PathVariable("etfId") Long etfId, @AuthenticationPrincipal String providerId) {

		Long userId = null; // default null

		// 로그인한 경우만 userId 세팅
		if (providerId != null) {
			User user = userService.getUserByProviderId(providerId);
			if (user != null) {
				userId = user.getUserId();
			}
		}

		EtfProduct etf = etfSearchService.selectOneEtf(etfId, userId);
		
		if (etf != null)
			return new ResponseEntity<>(etf, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}

	// 3. ETF 검색 및 정렬
	@GetMapping("/search")
	public ResponseEntity<?> search(@ModelAttribute SearchCondition con, @AuthenticationPrincipal String providerId) {

		// 1. 실제 검색
		List<EtfProduct> list = etfSearchService.searchByCondition(con);

		// 2. 로그인 확인
		// 2-1 로그인 여부 확인
		Long userId = null; // default null 로 설정

		if (providerId != null) {
			User user = userService.getUserByProviderId(providerId);
			if (user != null)
				userId = user.getUserId();
		}

		// 3. 검색 로그 저장
		if (con.getKeyword() != null && !con.getKeyword().isBlank()) {

			SearchLog log = new SearchLog();
			log.setUserId(userId);
			log.setKeyword(con.getKeyword());
			// 필요하면 추가 필드들: 필터 옵션, 정렬 기준 등
			searchLogService.insertSearchLog(log);
		}

		return ResponseEntity.ok(list);
	}

}
