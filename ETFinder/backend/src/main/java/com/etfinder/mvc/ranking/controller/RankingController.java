package com.etfinder.mvc.ranking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.ranking.dto.SearchRanking;
import com.etfinder.mvc.ranking.dto.ViewRanking;
import com.etfinder.mvc.ranking.service.SearchRankingService;
import com.etfinder.mvc.ranking.service.ViewRankingService;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

	private final ViewRankingService viewRankingService;
	private final SearchRankingService searchRankingService;
	
	
	public RankingController(ViewRankingService viewRankingService, SearchRankingService searchRankingService) {
		this.viewRankingService = viewRankingService;
		this.searchRankingService = searchRankingService;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// 1. 조회수 랭킹 
	// 1-1) 실시간 조회수 랭킹
	@GetMapping("/etf/view/hourly")
	public ResponseEntity<?> getHourlyViewRanking(){
		
		List<ViewRanking> list = viewRankingService.getHourlyViewRanking();
		
		if(list.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(list);
	}
	
	
	// 1-2) 일간 조회수 랭킹 
	@GetMapping("/etf/view/daily")
	public ResponseEntity<?> getDailyViewRanking(){
		
		List<ViewRanking> list = viewRankingService.getDailyViewRanking();
		
		if(list.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(list);
	}
	
	// 1-3) 월간 조회수 랭킹 
	@GetMapping("/etf/view/monthly")
	public ResponseEntity<?> getMonthlyViewRanking(){
		
		List<ViewRanking> list = viewRankingService.getMonthlyViewRanking();
		
		if(list.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(list);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	// 2. 검색어 랭킹 
	// 2-1) 실시간 검색어 랭킹
	@GetMapping("/search/hourly")
	public ResponseEntity<?> getHourlySearchRanking(){
		
		List<SearchRanking> list = searchRankingService.getHourlySearchRanking();
		
		if(list.isEmpty())
			return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	
	
	// 2-2) 일간 검색어 랭킹
	@GetMapping("/search/daily")
	public ResponseEntity<?> getDailySearchRanking(){
		
		List<SearchRanking> list = searchRankingService.getDailySearchRanking();
		
		if(list.isEmpty())
			return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	// 2-3) 월간 검색어 랭킹 
	@GetMapping("/search/monthly")
	public ResponseEntity<?> getMonthlySearchRanking(){
		
		List<SearchRanking> list = searchRankingService.getMonthlySearchRanking();
		
		if(list.isEmpty())
			return ResponseEntity.noContent().build();
		return ResponseEntity.ok(list);
	}
	
	
	
}
