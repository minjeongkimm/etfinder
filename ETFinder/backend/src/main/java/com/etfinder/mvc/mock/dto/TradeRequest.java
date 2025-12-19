package com.etfinder.mvc.mock.dto;

//주문 요청 dto
public class TradeRequest {
	private Long etfId;			
	private String tradeType;	//BUY / SELL
	private Integer quantity;
	
	public TradeRequest() {
	}

	public TradeRequest(Long etfId, String tradeType, Integer quantity) {
		this.etfId = etfId;
		this.tradeType = tradeType;
		this.quantity = quantity;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "TradeRequest [etfId=" + etfId + ", tradeType=" + tradeType + ", quantity=" + quantity + "]";
	}
	
	
}
