package com.etfinder.mvc.ranking.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.ranking.dto.SearchLog;
import com.etfinder.mvc.ranking.dto.SearchRanking;

@Mapper
public interface SearchLogMapper {

	// 1. 사용자별 검색 로그 저장
	int insertSearchLog(SearchLog log);

	// 2. 사용자별 검색 로그 조회 
	List<SearchLog> selectSearchLogByUser(@Param("userId") Long userId);
	
	// 3. 사용자별 특정 로그 삭제 (logId 기준)
	int deleteSearchLogById(@Param("logId") Long searchLogId);
	
	// 4. 사용자별 특정 키워드 로그 삭제
	int deleteSearchLogByKeyword(@Param("userId") Long userId,
			@Param("keyword") String keyword);
	
    // 5. 사용자별 검색 로그 전체 삭제 
    int deleteAllSearchLog(@Param("userId") Long userId);

}
