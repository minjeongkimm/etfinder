package com.etfinder.mvc.etf.dto;

import java.time.LocalDate;

public class EtfDailyHistory {

	private Long historyId;			// PK
	private Long etfId;				// FK
	private LocalDate baseDate;		// 기준 날짜
	private Integer closePrice;		// 종가 (차트에 사용)
	private Integer openPrice;		// 시가 
	private Integer highPrice;		// 고가 
	private Integer lowPrice;		// 저가 
	private Long volume;			// 거래량
	
	public EtfDailyHistory() {
	}

	public EtfDailyHistory(Long historyId, Long etfId, LocalDate baseDate, Integer closePrice, Integer openPrice,
			Integer highPrice, Integer lowPrice, Long volume) {
		this.historyId = historyId;
		this.etfId = etfId;
		this.baseDate = baseDate;
		this.closePrice = closePrice;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.volume = volume;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public LocalDate getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(LocalDate baseDate) {
		this.baseDate = baseDate;
	}

	public Integer getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Integer closePrice) {
		this.closePrice = closePrice;
	}

	public Integer getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(Integer openPrice) {
		this.openPrice = openPrice;
	}

	public Integer getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Integer highPrice) {
		this.highPrice = highPrice;
	}

	public Integer getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(Integer lowPrice) {
		this.lowPrice = lowPrice;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "EtfDailyHistory [historyId=" + historyId + ", etfId=" + etfId + ", baseDate=" + baseDate
				+ ", closePrice=" + closePrice + ", openPrice=" + openPrice + ", highPrice=" + highPrice + ", lowPrice="
				+ lowPrice + ", volume=" + volume + "]";
	}
	
}
