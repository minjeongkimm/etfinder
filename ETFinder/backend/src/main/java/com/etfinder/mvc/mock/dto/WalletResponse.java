package com.etfinder.mvc.mock.dto;

//지갑 요약 DTO (프론트 ui 표시용)
public class WalletResponse {
	private Long balance;			//가용 잔액
	private Long totalAsset;		//총 자산
	private Long realizedProfit;	//실현 손익 
	
	public WalletResponse() {
	}

	public WalletResponse(Long balance, Long totalAsset, Long realizedProfit) {
		this.balance = balance;
		this.totalAsset = totalAsset;
		this.realizedProfit = realizedProfit;
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

	public Long getRealizedProfit() {
		return realizedProfit;
	}

	public void setRealizedProfit(Long realizedProfit) {
		this.realizedProfit = realizedProfit;
	}

	@Override
	public String toString() {
		return "WalletSummary [balance=" + balance + ", totalAsset=" + totalAsset + ", realizedProfit=" + realizedProfit
				+ "]";
	}
	
	
}
