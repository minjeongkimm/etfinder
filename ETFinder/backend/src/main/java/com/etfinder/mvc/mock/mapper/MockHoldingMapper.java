package com.etfinder.mvc.mock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.MockHolding;
import com.etfinder.mvc.mock.dto.MockHoldingResponse;

@Mapper
public interface MockHoldingMapper {

	/*
     * 1. 거래 처리용 보유 종목 조회 (Row lock)
     * - SELECT ... FOR UPDATE
     * - holdings 테이블 엔티티만 조회
     */
	MockHolding selectForUpdate(@Param("userId")Long userId,
									 @Param("etfId") Long etfId);
	
    /*
     * 2. 새로운 보유 종목 추가
     * - holdings 테이블 insert
     */
	int insertHolding(MockHolding holding);
	
	/*
     * 3. 보유 종목 수량/평단가 수정
     * - holdings 테이블 update
     */
	int updateHolding(MockHolding holding);
	
	/*
     * 4. 보유 종목 삭제
     * - quantity 0되면 remove
     */
	int deleteHolding(@Param("userId")Long userId,
					@Param("etfId") Long etfId);
	
	/*
     * 5. 보유 종목 조회 (프론트 화면용 view)
     * - holdings + etf_product 
     * - UI 응답 구조
     */
	List<MockHoldingResponse> selectHoldingView(Long userId);
	
	/*
	 * 6. 보유 종목 조회 (존재 여부만 확인하는 용)
	 */
	MockHolding findByUserIdAndEtfId(@Param("userId") Long userId,
						@Param("etfId") Long etfId);
}
