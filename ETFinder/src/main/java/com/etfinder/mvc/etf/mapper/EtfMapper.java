package com.etfinder.mvc.etf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;

@Mapper
public interface EtfMapper {

	// 1. Etf 정보 등록
	// XML의 <insert id="insertEtf"> 와 연결됨
	void insertEtf(EtfProduct etf);

	// 2. Etf 일일 종가 갱신
	// etf_code로 찾아 current_price만 갱신
	void updateCurrentPrice(@Param("etfCode") String etfCode, @Param("currentPrice") Integer currentPrice);

	// 3. Etf 전체 조회
	List<EtfProduct> selectAllEtf();

	// 4. Etf 상세 조회
	EtfProduct selectOneEtf(Long etfId);

	// 5. Etf 검색, 정렬
	List<EtfProduct> searchByCondition(SearchCondition con);
}
