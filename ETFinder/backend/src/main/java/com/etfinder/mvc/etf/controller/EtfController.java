package com.etfinder.mvc.etf.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.service.EtfSearchService;
import com.etfinder.mvc.etf.service.EtfService;

@RestController
@RequestMapping("/api/etfs")
public class EtfController {

	private final EtfService etfService;
	private final EtfSearchService etfSearchService;

	public EtfController(EtfService etfService, EtfSearchService etfSearchService) {
		this.etfService = etfService;
		this.etfSearchService = etfSearchService;
	}

	@PostMapping
	public ResponseEntity<?> insertEtf(@RequestBody EtfProduct etf){
		int result = etfService.insertEtf(etf);
		
		if(result != 0) {
			return new ResponseEntity<String>("etf 등록이 완료되었습니다.", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<String>("etf 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/{etfCode}")
	public ResponseEntity<?> updateEtf(@PathVariable String etfCode, @RequestBody EtfProduct etf){
		int result = etfService.updateEtf(etfCode, etf);
		
		if(result != 0) {
			return new ResponseEntity<String>("etf 수정이 완료되었습니다.", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("etf 수정에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{etfCode}")
	public ResponseEntity<?> deleteEtf(@PathVariable String etfCode){
		int result = etfService.deleteEtf(etfCode);
		
		if(result != 0) {
			return new ResponseEntity<String>("etf 삭제가 완료되었습니다.", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("etf 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	// ai 설명 전체 db 적재
	@PostMapping("/init-ai-descriptions")
	public ResponseEntity<?> initializeAiDescriptions(){
		List<EtfProduct> allEtfs = etfSearchService.selectAllEtf();
		int successCnt = 0;
		int skipCnt = 0;
		
		for(EtfProduct etf : allEtfs) {
			// 이미 설명이 있으면 건너뛰기
            if (etf.getDescription() != null && !etf.getDescription().isEmpty()) {
                skipCnt++;
                continue;
            }
            try {
            	etfSearchService.updateEtfDescription(etf.getEtfId());
                successCnt++;
                // AI 서버 과부하/차단 방지를 위해 1초씩 쉬어줌
                Thread.sleep(1000);
                System.out.println("생성 완료: " + etf.getEtfName());
            } catch (Exception e) {
            	System.err.println("실패: " + etf.getEtfName());
            }
		}
		
		return ResponseEntity.ok(
	            String.format("완료! (생성됨: %d개, 이미있음: %d개)", successCnt, skipCnt)
	        );
	}
}
