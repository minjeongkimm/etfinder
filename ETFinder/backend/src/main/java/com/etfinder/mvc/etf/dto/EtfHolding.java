package com.etfinder.mvc.etf.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EtfHolding {

	private Long holdingId;				// PK
	private Long etfId;					// FK
	private String stockCode;			// 종목 코드
	private String stockName;			// 종목명 
	private BigDecimal weight;			// 구성 비율 
	private Integer stockPrice;			// 기준가(종가)
	private LocalDateTime updateAt;		// 갱신일
	
	public EtfHolding() {
	}

	public EtfHolding(Long holdingId, Long etfId, String stockCode, String stockName, BigDecimal weight,
			Integer stockPrice, LocalDateTime updateAt) {
		this.holdingId = holdingId;
		this.etfId = etfId;
		this.stockCode = stockCode;
		this.stockName = stockName;
		this.weight = weight;
		this.stockPrice = stockPrice;
		this.updateAt = updateAt;
	}

	public Long getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Long holdingId) {
		this.holdingId = holdingId;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Integer getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Integer stockPrice) {
		this.stockPrice = stockPrice;
	}

	public LocalDateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(LocalDateTime updateAt) {
		this.updateAt = updateAt;
	}

	@Override
	public String toString() {
		return "EtfHolding [holdingId=" + holdingId + ", etfId=" + etfId + ", stockCode=" + stockCode + ", stockName="
				+ stockName + ", weight=" + weight + ", stockPrice=" + stockPrice + ", updateAt=" + updateAt + "]";
	}
}
