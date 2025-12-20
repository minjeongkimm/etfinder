package com.etfinder.mvc.mock.service;

import java.util.List;

import com.etfinder.mvc.mock.dto.TradeHistoryResponse;
import com.etfinder.mvc.mock.dto.TradeRequest;

public interface TradeService {
	
	/**
	 * 매수/매도 거래 처리
	 * @param userId 유저 ID
	 * @param request 거래 요청 정보 (etfId, tradeType, quantity)
	 * @return 성공 여부 (1: 성공, 0: 실패)
	 */
	int executeTrade(Long userId, TradeRequest request);
	
	/**
	 * 최근 거래 내역 조회 (프론트 UI용)
	 * @param userId 유저 ID
	 * @return 거래 내역 리스트 (etf_product 조인하여 etfName 포함)
	 */
	List<TradeHistoryResponse> getRecentTrades(Long userId);
}

