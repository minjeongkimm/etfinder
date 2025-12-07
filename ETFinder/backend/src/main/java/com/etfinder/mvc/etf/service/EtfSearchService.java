package com.etfinder.mvc.etf.service;

import java.util.List;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;

public interface EtfSearchService {
	
	// 1. 전체 조회
	List<EtfProduct> selectAllEtf();
	
	// 2. 상세 조회 (상세 페이지)
	EtfProduct selectOneEtf(Long etfId, Long userId);
	
	// 3. 검색 
	List<EtfProduct> searchByCondition(SearchCondition con);

}
