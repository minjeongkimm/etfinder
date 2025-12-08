package com.etfinder.mvc.etf.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;

@Service
@Transactional
public class EtfServiceImpl implements EtfService{
	
	private final EtfMapper etfMapper;

	public EtfServiceImpl(EtfMapper etfMapper) {
		this.etfMapper = etfMapper;
	}

	@Override
	public int insertEtf(EtfProduct etf) {
		return etfMapper.insertEtf(etf);
	}

	@Override
	public int updateEtf(String etfCode, EtfProduct etf) {
		return etfMapper.updateEtf(etfCode, etf);
	}

	@Override
	public int deleteEtf(String etfCode) {
		return etfMapper.deleteEtf(etfCode);
	}

}
