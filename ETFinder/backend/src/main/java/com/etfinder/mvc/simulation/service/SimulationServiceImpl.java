package com.etfinder.mvc.simulation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.bookmark.dto.Bookmark;
import com.etfinder.mvc.bookmark.mapper.BookmarkMapper;
import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.simulation.dto.SimulationRequest;
import com.etfinder.mvc.simulation.dto.SimulationRequest.PortfolioItem;
import com.etfinder.mvc.simulation.dto.SimulationResponse;

@Service
public class SimulationServiceImpl implements SimulationService{
	
	@Autowired
	private EtfMapper etfMapper;
	
	@Autowired
	private BookmarkMapper bookmarkMapper;

	@Override
	public SimulationResponse simulate(Long userId, SimulationRequest request) {
		
		List<PortfolioItem> items = request.getPortfolio();
		if (items == null || items.isEmpty()) {
            // [CASE A: 기본 모드] 리스트가 없으면 -> DB에서 찜 목록 가져와서 1/n 계산
            return calculateEqualWeight(userId, request.getInvestmentAmount());
        } else {
            // [CASE B: 커스텀 모드] 리스트가 있으면 -> 사용자가 보낸 비율대로 계산
            return calculateCustomWeight(items, request.getInvestmentAmount());
        }
	}

	// 기본 모드
	private SimulationResponse calculateEqualWeight(Long userId, Long amount) {
		// 1. 찜한 ETF 목록 DB에서 가져오기
        List<Bookmark> bookmarks = bookmarkMapper.getBookmarksByUserId(userId);

        if (bookmarks.isEmpty()) {
            return emptyResponse(amount); // 찜한 게 없으면 0원 리턴
        }

        double totalYield = 0.0;
        int count = 0;

        // 2. 반복문 돌면서 수익률 합산 (단순 합계)
        for (Bookmark bookmark : bookmarks) {
            EtfProduct etf = etfMapper.selectOneEtf(bookmark.getEtfId());
            
            if (etf != null) {
                // null 체크: 수익률이 없으면 0.0%로 처리
                double yield = (etf.getReturn1yr() != null) ? etf.getReturn1yr() : 0.0;
                totalYield += yield;
                count++;
            }
        }

        // 3. 평균 수익률 계산 (총합 / 개수)
        double averageYield = (count > 0) ? (totalYield / count) : 0.0;

        // 4. 결과 반환
        return buildResponse(amount, averageYield, count);
	}
	
	// 커스텀 모드
	private SimulationResponse calculateCustomWeight(List<PortfolioItem> items, Long amount) {
		// 1. 비율 합계 검증 (100% 인지?)
        int totalRatio = items.stream().mapToInt(item -> item.getRatio()).sum();
        if (totalRatio != 100) {
            // 예외를 던져서 프론트에 알려주거나, 에러 메시지를 담은 응답을 줄 수 있어
            throw new IllegalArgumentException("비율의 합은 100%여야 합니다.");
        }

        double totalYield = 0.0;
        int count = 0;

        // 2. 반복문 돌면서 '가중 수익률' 계산
        for (SimulationRequest.PortfolioItem item : items) {
            // 사용자가 선택한 ETF 정보 조회
            EtfProduct etf = etfMapper.selectOneEtf(item.getEtfId());

            if (etf != null) {
                double yield = (etf.getReturn1yr() != null) ? etf.getReturn1yr() : 0.0;
                
                // ⭐ 핵심 공식: 수익률 * (내 비중 / 100)
                // 예: 수익률 10% * 비중 50% = 5.0
                totalYield += yield * (item.getRatio() / 100.0);
                count++;
            }
        }

        // 3. 결과 반환 (가중 평균은 이미 totalYield에 반영됨)
        return buildResponse(amount, totalYield, count);
	}
	
	// 응답 객체 생성
	private SimulationResponse buildResponse(Long amount, double yieldRate, int count) {
		// 수익금 계산
		long profit = (long) (amount * (yieldRate / 100.0));
		
		SimulationResponse response = new SimulationResponse();
		response.setTotalInvestment(amount);
		response.setTotalProfit(profit);
		response.setTotalYieldRate(Math.round(yieldRate * 100) / 100.0);
		response.setFinalAmount(amount + profit);
		response.setEtfCount(count);
		
		return response;
	}
	
	// 빈 응답 생성용
	private SimulationResponse emptyResponse(Long amount) {
		
		SimulationResponse response = new SimulationResponse();
		response.setTotalInvestment(amount);
		response.setTotalProfit(0L);
		response.setTotalYieldRate(0.0);
		response.setFinalAmount(amount);
		response.setEtfCount(0);
		
		return response;
	}



}
