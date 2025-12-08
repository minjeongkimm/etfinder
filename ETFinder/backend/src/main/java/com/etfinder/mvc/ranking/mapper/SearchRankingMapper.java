package com.etfinder.mvc.ranking.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.etfinder.mvc.ranking.dto.SearchRanking;

@Mapper
public interface SearchRankingMapper {

	// 1. 실시간 인기 검색어 랭킹 조회
	List<SearchRanking> getHourlySearchRanking();
	
	// 2. 일간 인기 검색어 랭킹 조회
	List<SearchRanking> getDailySearchRanking();
	
	// 3. 월간 인기 검색어 랭킹 조회
	List<SearchRanking> getMonthlySearchRanking();
	
}
