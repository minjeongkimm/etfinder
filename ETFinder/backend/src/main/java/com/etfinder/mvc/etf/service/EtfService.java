package com.etfinder.mvc.etf.service;

import com.etfinder.mvc.etf.dto.EtfProduct;

public interface EtfService {

	// etf 개별 등록
	int insertEtf(EtfProduct etf);
	
	// etf 수정 
	int updateEtf(String etfCode, EtfProduct etf);
	
	// etf 삭제 
	int deleteEtf(String etfCode);
}
