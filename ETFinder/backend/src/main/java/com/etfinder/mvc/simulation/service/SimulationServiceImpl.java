package com.etfinder.mvc.simulation.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.bookmark.dto.Bookmark;
import com.etfinder.mvc.bookmark.mapper.BookmarkMapper;
import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.simulation.dto.SimulationRequest;
import com.etfinder.mvc.simulation.dto.SimulationRequest.PortfolioItem;
import com.etfinder.mvc.simulation.dto.SimulationResponse;
import com.etfinder.mvc.simulation.dto.SimulationResponse.SimulationDetail;

@Service
public class SimulationServiceImpl implements SimulationService{
	
	private final EtfMapper etfMapper;
	private final BookmarkMapper bookmarkMapper;
	
	public SimulationServiceImpl(EtfMapper etfMapper, BookmarkMapper bookmarkMapper) {
		this.etfMapper = etfMapper;
		this.bookmarkMapper = bookmarkMapper;
	}

	@Override
	public SimulationResponse simulate(Long userId, SimulationRequest request) {
		
		List<PortfolioItem> items = request.getPortfolio();
		if (items == null || items.isEmpty()) {
            // [CASE A: 기본 모드] 사용자 설정 비율 없으면 -> DB에서 북마크(포트폴리오) 목록 가져와서 자동 1/n 계산
            return calculateEqualWeight(userId, request.getInvestmentAmount());
        } else {
            // [CASE B: 커스텀 모드] 사용자 설정 비율 있으면 -> 비율대로 계산
            return calculateCustomWeight(items, request.getInvestmentAmount());
        }
	}

	// 기본 모드
	private SimulationResponse calculateEqualWeight(Long userId, Long amount) {
		// 1. 북마크한 ETF 목록 DB에서 가져오기
        List<Bookmark> bookmarks = bookmarkMapper.getBookmarksByUserId(userId);

        if (bookmarks.isEmpty()) {
            return emptyResponse(amount); // 북마크한 게 없으면 0원 리턴
        }
     // 2. 유효한 ETF만 먼저 골라내기 (1/n 정확히 나누기 위해)
        List<EtfProduct> validEtfs = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            EtfProduct etf = etfMapper.selectOneEtf(bookmark.getEtfId());
            if (etf != null) {
                validEtfs.add(etf);
            }
        }
        
        // 유효한 ETF가 하나도 없으면 빈 응답
        int count = validEtfs.size();
        if (count == 0) {
        	return emptyResponse(amount);
        }

        // 3. 계산 및 상세 내역(Detail) 생성
        List<SimulationDetail> details = new ArrayList<>();
        double totalReturnRate = 0.0;
        
        // 1/n 비중 계산 (소수점 고려하여 계산용으로 사용, 화면 표시는 정수로 근사치)
        int equalRatio = 100 / count; 
        
        for (EtfProduct etf : validEtfs) {
            // 수익률 null 체크
            double returnRate = (etf.getReturn1yr() != null) ? etf.getReturn1yr() : 0.0;
            totalReturnRate += returnRate;

            // 개별 수익금 계산 (총 투자금 / 개수 * 수익률)
            long eachInvestment = amount / count;
            long eachProfit = (long) (eachInvestment * (returnRate / 100.0));
            
            // 상세 정보 리스트에 추가
            SimulationDetail detail = new SimulationDetail();
            detail.setEtfName(etf.getEtfName());
            detail.setRatio(equalRatio);
            detail.setReturnRate1y(returnRate);
            detail.setProfit(eachProfit);
            
            details.add(detail);
        }

        // 4. 전체 평균 수익률 계산
        double averageReturnRate = totalReturnRate / count;

        // 5. 결과 반환 (+상세 리스트 포함)
        return buildResponse(amount, averageReturnRate, count, details);
	}
	
	// 커스텀 모드
	private SimulationResponse calculateCustomWeight(List<PortfolioItem> items, Long amount) {
		// 1. 비율 합계 검증 (100% 인지?)
        int totalRatio = items.stream().mapToInt(item -> item.getRatio()).sum();
        if (totalRatio != 100) {
            // 예외를 던져서 프론트에 알려주거나, 에러 메시지를 담은 응답 주기
            throw new IllegalArgumentException("비율의 합은 100%여야 합니다.");
        }

        List<SimulationDetail> details = new ArrayList<>();
        double totalWeightedReturnRate = 0.0;
        int count = 0;

        // 2. 반복문 돌면서 '가중 수익률' 계산
        for (PortfolioItem item : items) {
            // 사용자가 선택한 ETF 정보 조회
            EtfProduct etf = etfMapper.selectOneEtf(item.getEtfId());

            if (etf != null) {
                double returnRate = (etf.getReturn1yr() != null) ? etf.getReturn1yr() : 0.0;
                int ratio = item.getRatio();
                
                // ⭐ 핵심 공식: 수익률 * (내 비중 / 100)
                // 예: 수익률 10% * 비중 50% = 5.0
                totalWeightedReturnRate += returnRate * (ratio / 100.0);
                long eachProfit = (long) (amount * (ratio / 100.0) * (returnRate / 100.0));
                
                SimulationDetail detail = new SimulationDetail();
                detail.setEtfName(etf.getEtfName());
                detail.setRatio(ratio);
                detail.setReturnRate1y(returnRate);
                detail.setProfit(eachProfit);
                
                details.add(detail);
                
                count++;
            }
        }

        // 3. 결과 반환 (가중 평균은 이미 totalReturnRate에 반영됨)
        return buildResponse(amount, totalWeightedReturnRate, count, details);
	}
	
	// 응답 객체 생성
	private SimulationResponse buildResponse(Long amount, double returnRate, int count, List<SimulationDetail> details) {
		// 수익금 계산
		long profit = (long) (amount * (returnRate / 100.0));
		
		SimulationResponse response = new SimulationResponse();
		response.setTotalInvestment(amount);
		response.setTotalProfit(profit);
		response.setTotalReturnRate(Math.round(returnRate * 100) / 100.0);
		response.setFinalAmount(amount + profit);
		response.setEtfCount(count);
		response.setDetails(details);
		
		return response;
	}
	
	// 빈 응답 생성용
	private SimulationResponse emptyResponse(Long amount) {
		
		SimulationResponse response = new SimulationResponse();
		response.setTotalInvestment(amount);
		response.setTotalProfit(0L);
		response.setTotalReturnRate(0.0);
		response.setFinalAmount(amount);
		response.setEtfCount(0);
		response.setDetails(new ArrayList<>());
		
		return response;
	}
}
