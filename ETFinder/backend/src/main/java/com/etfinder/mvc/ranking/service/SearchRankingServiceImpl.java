package com.etfinder.mvc.ranking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.ranking.dto.SearchRanking;
import com.etfinder.mvc.ranking.mapper.SearchRankingMapper;

@Service
public class SearchRankingServiceImpl implements SearchRankingService {

	private final SearchRankingMapper searchRankingMapper;
	
	
	
	public SearchRankingServiceImpl(SearchRankingMapper searchRankingMapper) {
		this.searchRankingMapper = searchRankingMapper;
	}

	
	// 1. 실시간 검색어 랭킹 
	@Override
	public List<SearchRanking> getHourlySearchRanking() {
		return searchRankingMapper.getHourlySearchRanking();
	}

	// 2. 일간 검색어 랭킹 
	@Override
	public List<SearchRanking> getDailySearchRanking() {
		return searchRankingMapper.getDailySearchRanking();
	}

	// 3. 월간 검색어 랭킹 
	@Override
	public List<SearchRanking> getMonthlySearchRanking() {
		return searchRankingMapper.getMonthlySearchRanking();
	}

}
