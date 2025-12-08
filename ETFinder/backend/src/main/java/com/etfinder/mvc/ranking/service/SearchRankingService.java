package com.etfinder.mvc.ranking.service;

import java.util.List;

import com.etfinder.mvc.ranking.dto.SearchRanking;

public interface SearchRankingService {

	// 1. 실시간 인기 검색어 랭킹 조회
	List<SearchRanking> getHourlySearchRanking();
	
	// 2. 일간 인기 검색어 랭킹 조회
	List<SearchRanking> getDailySearchRanking();
	
	// 3. 월간 인기 검색어 랭킹 조회
	List<SearchRanking> getMonthlySearchRanking();
	
}
