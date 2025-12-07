package com.etfinder.mvc.ranking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.ranking.dto.SearchLog;
import com.etfinder.mvc.ranking.mapper.SearchLogMapper;

@Service
public class SearchLogServiceImpl implements SearchLogService {

	private final SearchLogMapper searchLogMapper;
	
	
	public SearchLogServiceImpl(SearchLogMapper searchLogMapper) {
		this.searchLogMapper = searchLogMapper;
	}

	
	// 1. 사용자별 검색 로그 저장 
	@Override
	public int insertSearchLog(SearchLog log) {
		return searchLogMapper.insertSearchLog(log);
	}

	
	// 2. 사용자별 검색 로그 조회 
	@Override
	public List<SearchLog> selectSearchLogByUser(Long userId) {
		return searchLogMapper.selectSearchLogByUser(userId);
	}

	// 3. 사용자별 특정 로그 삭제 (logId 기준) 
	@Override
	public int deleteSearchLogById(Long searchLogId) {
		return searchLogMapper.deleteSearchLogById(searchLogId);
	}

	// 4. 사용자별 특정 키워드 로그 삭제
	@Override
	public int deleteSearchLogByKeyword(Long userId, String keyword) {
		return searchLogMapper.deleteSearchLogByKeyword(userId, keyword);
	}

	// 5. 사용자별 검색 로그 전체 삭제 
	@Override
	public int deleteAllSearchLog(Long userId) {
		return searchLogMapper.deleteAllSearchLog(userId);
	}

}
