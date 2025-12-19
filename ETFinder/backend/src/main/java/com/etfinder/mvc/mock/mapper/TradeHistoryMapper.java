package com.etfinder.mvc.mock.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.TradeHistory;

@Mapper
public interface TradeHistoryMapper {
	
	//1. 거래 내역 추가
	int insert(TradeHistory trade);
	
	//2. 최근 거래 내역 조회
	List<TradeHistory> selectRecentTrades(@Param("userId") Long userId);	
	
	//3. 전체 거래 내역 조회
	List<TradeHistory> selectTradesUpToDate(@Param("userId") Long userId,
											@Param("toDate") LocalDate toDate);
	
	//4. 특정 기간동안 거래 내역 조회
	List<TradeHistory> selectTradesBetween(@Param("userId") Long userId,
											@Param("from") LocalDateTime from,
											@Param("to") LocalDateTime to);
	
	
	
}
