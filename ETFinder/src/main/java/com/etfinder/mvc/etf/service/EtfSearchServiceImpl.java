package com.etfinder.mvc.etf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;
import com.etfinder.mvc.etf.mapper.EtfMapper;

@Service
public class EtfSearchServiceImpl implements EtfSearchService {

	@Autowired
	private EtfMapper etfMapper;
	
	@Override
	public List<EtfProduct> selectAllEtf() {
		
		return etfMapper.selectAllEtf();
	}

	@Override
	public EtfProduct selectOneEtf(Long etfId) {
		return etfMapper.selectOneEtf(etfId);
	}

	@Override
	public List<EtfProduct> searchByCondition(SearchCondition con) {
		return etfMapper.searchByCondition(con);
	}

}
