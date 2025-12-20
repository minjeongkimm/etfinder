package com.etfinder.mvc.mock.mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.EtfDailyPrice;

@Mapper
public interface EtfDailyHistoryMapper {
	
	//날짜별 가격 조회
	List<EtfDailyPrice> selectClosePriceByDate(
			@Param("etfIds") Collection<Long> etfIds,
			@Param("baseDate") LocalDate baseDate
			);
	
}
