package com.etfinder.mvc.mock.dto;

import java.time.LocalDateTime;

//지갑 dto
public class Wallet {
	private Long userId; 						//유저 아이디
	private Long balance; 						//가용 잔액
	private Long totalAsset;					//총 자산
	private LocalDateTime updatedAt;			//수정 일자
	private Long initialDeposit = 10000000L;	//총 자산 기본 설정 값 

	public Wallet() {
	}

	public Wallet(Long userId, Long balance, Long totalAsset, LocalDateTime updatedAt, Long initialDeposit) {
		this.userId = userId;
		this.balance = balance;
		this.totalAsset = totalAsset;
		this.updatedAt = updatedAt;
		this.initialDeposit = initialDeposit;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Long totalAsset) {
		this.totalAsset = totalAsset;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getInitialDeposit() {
		return initialDeposit;
	}

	public void setInitialDeposit(Long initialDeposit) {
		this.initialDeposit = initialDeposit;
	}

	@Override
	public String toString() {
		return "Wallet [userId=" + userId + ", balance=" + balance + ", totalAsset=" + totalAsset + ", updatedAt="
				+ updatedAt + ", initialDeposit=" + initialDeposit + "]";
	}

	

}
