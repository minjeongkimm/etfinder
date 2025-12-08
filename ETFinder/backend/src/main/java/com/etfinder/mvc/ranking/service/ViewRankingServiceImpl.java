package com.etfinder.mvc.ranking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.ranking.dto.ViewRanking;
import com.etfinder.mvc.ranking.mapper.ViewRankingMapper;

@Service
public class ViewRankingServiceImpl implements ViewRankingService {

	private final ViewRankingMapper viewRankingMapper;

	
	public ViewRankingServiceImpl(ViewRankingMapper viewRankingMapper) {
		this.viewRankingMapper = viewRankingMapper;
	}


	// 1. 실시간 조회수 랭킹 조회 
	@Override
	public List<ViewRanking> getHourlyViewRanking() {
		return viewRankingMapper.getHourlyViewRanking();
	}

	
	// 2. 일간 조회수 랭킹 조회 
	@Override
	public List<ViewRanking> getDailyViewRanking() {
		return viewRankingMapper.getDailyViewRanking();
	}

	
	// 3. 월간 조회수 랭킹 조회 
	@Override
	public List<ViewRanking> getMonthlyViewRanking() {
		return viewRankingMapper.getMonthlyViewRanking();
	}

}
