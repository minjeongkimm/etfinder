package com.etfinder.mvc.ranking.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.ranking.dto.SearchLog;

@Mapper
public interface SearchLogMapper {

	// 1. 사용자별 검색 로그 저장
	int insertSearchLog(SearchLog log);

	// 2. 사용자별 검색 로그 조회 
	List<SearchLog> selectSearchLogByUser(@Param("userId") Long userId);
	
	// 3. 사용자별 특정 로그 삭제 (logId 기준)
	int deleteSearchLogById(@Param("userId") Long userId, @Param("searchLogId") Long searchLogId);
	
    // 4. 사용자별 검색 로그 전체 삭제 
    int deleteAllSearchLog(@Param("userId") Long userId);

}
