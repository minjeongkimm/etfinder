package com.etfinder.mvc.etf.controller;

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
import com.etfinder.mvc.etf.service.EtfService;

@RestController
@RequestMapping("/api/etfs")
public class EtfController {

	private final EtfService etfService;

	public EtfController(EtfService etfService) {
		this.etfService = etfService;
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
}
