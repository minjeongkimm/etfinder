package com.etfinder.mvc.mock.dto;

//보유 종목 DTO (대시보드 UI 용) 
public class MockHoldingResponse {
	private Long etfId;
	private String etfCode;
	private String etfName;
	
	private Integer quantity;		//보유 수량
	private Integer averagePrice;	//평단가
	private Integer currentPrice;	//종가
	
	private Long evalAmount;		//평가 금액
	private Long profitAmount;		//평가 손익
	private Double profitRate;		//평가 수익률 
	
	public MockHoldingResponse() {
	}

	public MockHoldingResponse(Long etfId, String etfCode, String etfName, Integer quantity, Integer averagePrice,
			Integer currentPrice, Long evalAmount, Long profitAmount, Double profitRate) {
		this.etfId = etfId;
		this.etfCode = etfCode;
		this.etfName = etfName;
		this.quantity = quantity;
		this.averagePrice = averagePrice;
		this.currentPrice = currentPrice;
		this.evalAmount = evalAmount;
		this.profitAmount = profitAmount;
		this.profitRate = profitRate;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public String getEtfCode() {
		return etfCode;
	}

	public void setEtfCode(String etfCode) {
		this.etfCode = etfCode;
	}

	public String getEtfName() {
		return etfName;
	}

	public void setEtfName(String etfName) {
		this.etfName = etfName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(Integer averagePrice) {
		this.averagePrice = averagePrice;
	}

	public Integer getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Integer currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Long getEvalAmount() {
		return evalAmount;
	}

	public void setEvalAmount(Long evalAmount) {
		this.evalAmount = evalAmount;
	}

	public Long getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(Long profitAmount) {
		this.profitAmount = profitAmount;
	}

	public Double getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(Double profitRate) {
		this.profitRate = profitRate;
	}

	@Override
	public String toString() {
		return "HoldingResponse [etfId=" + etfId + ", etfCode=" + etfCode + ", etfName=" + etfName + ", quantity="
				+ quantity + ", averagePrice=" + averagePrice + ", currentPrice=" + currentPrice + ", evalAmount="
				+ evalAmount + ", profitAmount=" + profitAmount + ", profitRate=" + profitRate + "]";
	}

	
}
