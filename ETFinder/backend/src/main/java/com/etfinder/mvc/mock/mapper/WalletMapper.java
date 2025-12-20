package com.etfinder.mvc.mock.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.etfinder.mvc.mock.dto.Wallet;

@Mapper
public interface WalletMapper {
	
	//1. 지갑 조회
	Wallet selectByUserId(Long userId);
	
	/* 2. 지갑 초기 설정 (없는 경우)
	 * 	  balance    = 10,000,000
	 * 	  totalAsset = 10,000,000
	 */
	int insertInitialWallet(Long userId);	
	
	//3. 가용 잔액 업데이트
	int updateBalance(Long userId, Long balance);
	
	//4. 총 자산 업데이트
	int updateTotalAsset(Long userId, Long totalAsset);
	
	//5. 지갑 리셋 
	int deleteByUserId(Long userId);
}
