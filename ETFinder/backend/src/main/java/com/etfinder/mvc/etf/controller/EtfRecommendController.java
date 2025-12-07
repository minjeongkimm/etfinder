package com.etfinder.mvc.etf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.etf.dto.EtfRecommendResponse;
import com.etfinder.mvc.etf.service.EtfRecommendService;

@RestController
@RequestMapping("/api/etfs/recommend")
public class EtfRecommendController {

	@Autowired
	private EtfRecommendService etfRecommendService;
	
	@GetMapping
	public ResponseEntity<?> getRecommendation(@AuthenticationPrincipal String providerId){
		List<EtfRecommendResponse> result = etfRecommendService.recommend(providerId);
		
		return new ResponseEntity<List<EtfRecommendResponse>>(result, HttpStatus.OK);
	}
	
}
