package com.etfinder.mvc.ranking.service;

import java.util.List;

import com.etfinder.mvc.ranking.dto.ViewRanking;

public interface ViewRankingService {
	
	// 1. 실시간 조회수 랭킹 조회
	List<ViewRanking> getHourlyViewRanking();

	// 2. 일간 조회수 랭킹 조회
	List<ViewRanking> getDailyViewRanking();

	// 3. 월간 조회수 랭킹 조회
	List<ViewRanking> getMonthlyViewRanking();

}
