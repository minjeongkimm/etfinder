package com.etfinder.mvc.mock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.MockRanking;

@Mapper
public interface MockRankingMapper {

	//1. 상위랭크 유저 조회 (수익률 기준)
	List<MockRanking> selectTopRankers(@Param("limit") int limit);
	
	//2. 유저 랭크 조회
	MockRanking selectMyRank(@Param("userId") Long userId);
	
	//3. 랭킹 전체 유저 수 계산
	int countRankUsers();
	
}
