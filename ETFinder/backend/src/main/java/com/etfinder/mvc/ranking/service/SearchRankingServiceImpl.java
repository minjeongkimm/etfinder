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
		List<SearchRanking> currentRankings = searchRankingMapper.getHourlySearchRanking();

		// 데이터 부족 시(3개 미만) 일간 랭킹으로 대체
		if (currentRankings.size() < 3) {
			return getDailySearchRanking();
		}

		List<SearchRanking> pastRankings = searchRankingMapper.getPastHourlySearchRanking();
		calculateRankChange(currentRankings, pastRankings);

		return currentRankings;
	}

	// 2. 일간 검색어 랭킹
	@Override
	public List<SearchRanking> getDailySearchRanking() {
		List<SearchRanking> currentRankings = searchRankingMapper.getDailySearchRanking();
		List<SearchRanking> pastRankings = searchRankingMapper.getPastDailySearchRanking();

		calculateRankChange(currentRankings, pastRankings);

		return currentRankings;
	}

	// 3. 월간 검색어 랭킹
	@Override
	public List<SearchRanking> getMonthlySearchRanking() {
		List<SearchRanking> currentRankings = searchRankingMapper.getMonthlySearchRanking();
		List<SearchRanking> pastRankings = searchRankingMapper.getPastMonthlySearchRanking();

		calculateRankChange(currentRankings, pastRankings);

		return currentRankings;
	}

	// 순위 변동 계산 헬퍼 메소드
	private void calculateRankChange(List<SearchRanking> currentRankings, List<SearchRanking> pastRankings) {
		for (int i = 0; i < currentRankings.size(); i++) {
			SearchRanking current = currentRankings.get(i);
			int currentRank = i + 1;
			Integer pastRank = null;

			// 과거 랭킹에서 동일 키워드 찾기
			for (int j = 0; j < pastRankings.size(); j++) {
				if (pastRankings.get(j).getKeyword().equals(current.getKeyword())) {
					pastRank = j + 1;
					break;
				}
			}

			if (pastRank != null) {
				// 순위 변동: 과거 순위 - 현재 순위 (양수면 상승, 음수면 하락)
				current.setRankChange(pastRank - currentRank);
			} else {
				// 신규 진입 (null)
				current.setRankChange(null);
			}
		}
	}

}
