package com.etfinder.mvc.ranking.service;

import java.util.List;


import com.etfinder.mvc.ranking.dto.SearchLog;

public interface SearchLogService {

	// 1. 사용자별 검색 로그 저장
	int insertSearchLog(SearchLog log);

	// 2. 사용자별 검색 로그 조회 
	List<SearchLog> selectSearchLogByUser(Long userId);
	
	// 3. 특정 로그 삭제 (logId 기준)
	int deleteSearchLogById(Long searchLogId);
	
	// 4. 특정 키워드 전부 삭제
	int deleteSearchLogByKeyword(Long userId, String keyword);
	
    // 5. 전체 삭제 (유저별 전체)
    int deleteAllSearchLog(Long userId);
	
	
}
