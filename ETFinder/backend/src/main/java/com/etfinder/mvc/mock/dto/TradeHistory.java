package com.etfinder.mvc.mock.dto;

import java.time.LocalDateTime;

//거래 내역 DTO
public class TradeHistory {

	private Long tradeId;		//거래 id
	private Long etfId;			//etf id
	private String etfName;		//etf 종목 이름 

	private String tradeType;	 //BUY / SELL
	private Integer price;		 //체결 단가
	private Integer quantity;	 //체결 수량
	private Long amount;		//총 거래액 (price * quantity)
	private LocalDateTime createdAt;

	public TradeHistory() {
	}

	public TradeHistory(Long tradeId, Long etfId, String etfName, String tradeType, Integer price, Integer quantity,
			Long amount, LocalDateTime createdAt) {
		this.tradeId = tradeId;
		this.etfId = etfId;
		this.etfName = etfName;
		this.tradeType = tradeType;
		this.price = price;
		this.quantity = quantity;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public String getEtfName() {
		return etfName;
	}

	public void setEtfName(String etfName) {
		this.etfName = etfName;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "TradeHistory [tradeId=" + tradeId + ", etfId=" + etfId + ", etfName=" + etfName + ", tradeType="
				+ tradeType + ", price=" + price + ", quantity=" + quantity + ", amount=" + amount + ", createdAt="
				+ createdAt + "]";
	}

}
