package com.etfinder.mvc.ranking.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.etfinder.mvc.ranking.dto.ViewRanking;

@Mapper
public interface ViewRankingMapper {
	
	// 1. 실시간 조회수 랭킹 조회 
	List<ViewRanking> getHourlyViewRanking();
	
	// 2. 일간 조회수 랭킹 조회 
	List<ViewRanking> getDailyViewRanking();
	
	// 3. 월간 조회수 랭킹 조회 
	List<ViewRanking> getMonthlyViewRanking();
	
	// 4. 조회수 증가 
	int increaseViewCount(Long etfId);
	
}
