package com.etfinder.mvc.mock.service;

import com.etfinder.mvc.mock.dto.MockRankingResponse;

public interface MockRankingService {
	
	/**
	 * 모의투자 랭킹 정보 조회
	 * @param userId 유저 ID
	 * @return 랭킹 정보 (내 순위, 전체 유저 수, 상위 퍼센트, 상위 랭커 목록)
	 */
	MockRankingResponse getRankingInfo(Long userId);
}

