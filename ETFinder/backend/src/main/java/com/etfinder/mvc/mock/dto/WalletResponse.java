package com.etfinder.mvc.mock.dto;

//지갑 요약 DTO (프론트 ui 표시용)
public class WalletResponse {
	private Long balance;			//가용 잔액
	private Long totalAsset;		//총 자산
	private Long realizedProfit;	//실현 손익 
	
	private Long cashRatio;   // 현금 보유 비율  
	private Long stockRatio;  // 주식 보유 비율
	
	public WalletResponse() {
	}

	// 3개 매개변수 생성자 (하위 호환성)
	public WalletResponse(Long balance, Long totalAsset, Long realizedProfit) {
		this.balance = balance;
		this.totalAsset = totalAsset;
		this.realizedProfit = realizedProfit;
		this.cashRatio = 0L;
		this.stockRatio = 0L;
	}

	public WalletResponse(Long balance, Long totalAsset, Long realizedProfit, Long cashRatio, Long stockRatio) {
		this.balance = balance;
		this.totalAsset = totalAsset;
		this.realizedProfit = realizedProfit;
		this.cashRatio = cashRatio;
		this.stockRatio = stockRatio;
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

	public Long getCashRatio() {
		return cashRatio;
	}

	public void setCashRatio(Long cashRatio) {
		this.cashRatio = cashRatio;
	}

	public Long getStockRatio() {
		return stockRatio;
	}

	public void setStockRatio(Long stockRatio) {
		this.stockRatio = stockRatio;
	}

	@Override
	public String toString() {
		return "WalletResponse [balance=" + balance + ", totalAsset=" + totalAsset + ", realizedProfit="
				+ realizedProfit + ", cashRatio=" + cashRatio + ", stockRatio=" + stockRatio + "]";
	}

	
	
}
