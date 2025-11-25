package com.etfinder.mvc.service;

import java.util.List;

import com.etfinder.mvc.model.dto.EtfProduct;
import com.etfinder.mvc.model.dto.SearchCondition;

public interface EtfSearchService {
	
	// 1. 전체 조회
	List<EtfProduct> selectAllEtf();
	
	// 2. 상세 조회 (상세 페이지)
	EtfProduct selectOneEtf(Long etfId);
	
	// 3. 검색 
	List<EtfProduct> searchByCondition(SearchCondition con);

}
