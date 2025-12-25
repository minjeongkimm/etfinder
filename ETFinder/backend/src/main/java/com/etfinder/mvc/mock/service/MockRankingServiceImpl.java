package com.etfinder.mvc.mock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.mock.dto.MockRanking;
import com.etfinder.mvc.mock.dto.MockRankingResponse;
import com.etfinder.mvc.mock.mapper.MockRankingMapper;

@Service
@Transactional(readOnly = true)
public class MockRankingServiceImpl implements MockRankingService {

	private final MockRankingMapper rankingMapper;
	
	public MockRankingServiceImpl(MockRankingMapper rankingMapper) {
		this.rankingMapper = rankingMapper;
	}

	@Override
	public MockRankingResponse getRankingInfo(Long userId) {
		
		// 1. 상위 랭커 조회 (예: Top 10)
		List<MockRanking> topUsers = rankingMapper.selectTopRankers(10);
		
		// 2. 내 랭킹 조회
		MockRanking myRanking = rankingMapper.selectMyRank(userId);
		
		// 3. 전체 유저 수 조회
		int totalUsers = rankingMapper.countRankUsers();
		
		// 4. 응답 구성
		MockRankingResponse response = new MockRankingResponse();
		response.setTopUsers(topUsers);
		response.setTotalUsers(totalUsers);
		
		if (myRanking != null) {
			response.setMyRank(myRanking.getRank());
			
			// 상위 퍼센트 계산
			if (totalUsers > 0) {
				double topPercent = (myRanking.getRank() * 100.0) / totalUsers;
				response.setTopPercent(Math.round(topPercent * 100.0) / 100.0);
			}
		}
		
		return response;
	}
}

