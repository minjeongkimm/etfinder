package com.etfinder.mvc.etf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;
import com.etfinder.mvc.etf.service.EtfSearchService;

@RestController
@RequestMapping("/api/etfs")
public class EtfSearchController {

	private final EtfSearchService etfSearchService;
	
	public EtfSearchController(EtfSearchService etfSearchService) {
		this.etfSearchService = etfSearchService;
	}
	
	// 1. 전체 조회
	@GetMapping
	public ResponseEntity<?> list(){
		List<EtfProduct> list = etfSearchService.selectAllEtf();
		
		if(list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<EtfProduct>>(list, HttpStatus.OK);
		
	}
	
	// 2. 상세 조회
	@GetMapping("/{etfId}")
	public ResponseEntity<?> detail(@PathVariable("etfId") Long etfId){
		EtfProduct etfProduct = etfSearchService.selectOneEtf(etfId);
		
		if(etfProduct != null)
			return new ResponseEntity<EtfProduct>(etfProduct, HttpStatus.OK);
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}
	
	
	// 3. ETF 검색 및 정렬 
	@GetMapping("/search")
	public ResponseEntity<?> search(@ModelAttribute SearchCondition con){
		List<EtfProduct> list = etfSearchService.searchByCondition(con);

		if(list == null || list.size() == 0)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<EtfProduct>>(list, HttpStatus.OK);
		
	}
 	
	
}
