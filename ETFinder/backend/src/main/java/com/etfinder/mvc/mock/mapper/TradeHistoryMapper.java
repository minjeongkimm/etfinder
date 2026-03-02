package com.etfinder.mvc.mock.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.TradeHistory;
import com.etfinder.mvc.mock.dto.TradeHistoryResponse;

@Mapper
public interface TradeHistoryMapper {
	
	//1. 거래 내역 추가
	int insert(TradeHistory trade);
	
	//2. 최근 거래 내역 조회 (프론트 UI용, etf_product 조인)
	List<TradeHistoryResponse> selectRecentTrades(@Param("userId") Long userId);	
	
	//3. 전체 거래 내역 조회
	List<TradeHistory> selectTradesUpToDate(@Param("userId") Long userId,
											@Param("toDate") LocalDate toDate);
	
	//4. 특정 기간동안 거래 내역 조회
	List<TradeHistory> selectTradesBetween(@Param("userId") Long userId,
											@Param("from") LocalDateTime from,
											@Param("to") LocalDateTime to);
	
	//5. 유저의 거래 내역 전체 삭제 (계좌 리셋 용)
	int deleteAllByUserId(@Param("userId") Long userId);
	
}
