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
import com.etfinder.mvc.etf.history.EtfDailyHistoryUpdateService;
import com.etfinder.mvc.etf.service.EtfSearchService;
import com.etfinder.mvc.etf.service.EtfService;

@RestController
@RequestMapping("/api/etfs")
public class EtfController {

	private final EtfService etfService;
	private final EtfSearchService etfSearchService;
	private final EtfDailyHistoryUpdateService etfDailyHistoryUpdateService;

	public EtfController(EtfService etfService, EtfSearchService etfSearchService,
			EtfDailyHistoryUpdateService etfDailyHistoryUpdateService) {
		this.etfService = etfService;
		this.etfSearchService = etfSearchService;
		this.etfDailyHistoryUpdateService = etfDailyHistoryUpdateService;
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
		
		new Thread(() -> {
			List<EtfProduct> allEtfs = etfSearchService.selectAllEtf();
			
			for(EtfProduct etf : allEtfs) {
				// 이미 설명이 있으면 건너뛰기
	            if (etf.getDescription() != null && !etf.getDescription().isEmpty()) {
	                continue;
	            }
	            try {
	            	etfSearchService.updateEtfDescription(etf.getEtfId());
	                // AI 서버 과부하/차단 방지를 위해 1초씩 쉬어줌
	                Thread.sleep(1000);
	                System.out.println("생성 완료: " + etf.getEtfName());
	            } catch (Exception e) {
	            	System.err.println("실패: " + etf.getEtfName());
	            }
			}
			System.out.println(">>> AI 전체 설명 생성 완료!");
		}).start();
		
		return ResponseEntity.ok("AI 설명 생성이 백그라운드에서 시작되었습니다.");
	}
	
	// 과거 시세 1년치 db 적재
	@PostMapping("/init-history")
	public ResponseEntity<?> initializeHistory(){
		
		// 비동기 실행
		new Thread(() -> {
			System.out.println(">>> ETF 과거 시세 적재 시작 (백그라운드 실행)");
			etfDailyHistoryUpdateService.loadAllEtfsHistory();
			System.out.println(">>> ETF 과거 시세 적재 완료");
		}).start();
		
		return new ResponseEntity<String>("ETF 과거 시세(1년치) 적재가 백그라운드에서 시작되었습니다.", HttpStatus.OK);
	}
}
