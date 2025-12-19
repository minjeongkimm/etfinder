package com.etfinder.mvc.mock.dto;

import java.time.LocalDate;

public class EtfDailyPrice {
	
	private Long etfId;					
	private Long closePrice;		//종가
	private LocalDate baseDate;		//기준 날짜
	
	public EtfDailyPrice() {
	}

	public EtfDailyPrice(Long etfId, Long closePrice, LocalDate baseDate) {
		this.etfId = etfId;
		this.closePrice = closePrice;
		this.baseDate = baseDate;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public Long getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Long closePrice) {
		this.closePrice = closePrice;
	}

	public LocalDate getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(LocalDate baseDate) {
		this.baseDate = baseDate;
	}

	@Override
	public String toString() {
		return "EtfDailyPrice [etfId=" + etfId + ", closePrice=" + closePrice + ", baseDate=" + baseDate + "]";
	}
	
	
}
