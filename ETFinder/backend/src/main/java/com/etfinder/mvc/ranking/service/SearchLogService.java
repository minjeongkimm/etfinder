package com.etfinder.mvc.ranking.service;

import java.util.List;


import com.etfinder.mvc.ranking.dto.SearchLog;

public interface SearchLogService {

	// 1. 사용자별 검색 로그 저장
	int insertSearchLog(SearchLog log);

	// 2. 사용자별 검색 로그 조회 
	List<SearchLog> selectSearchLogByUser(Long userId);
	
	// 3. 특정 로그 삭제 (logId 기준)
	int deleteSearchLogById(Long userId, Long searchLogId);
	
    // 4. 로그 전체 삭제 (유저별 전체)
    int deleteAllSearchLog(Long userId);
	
	
}
