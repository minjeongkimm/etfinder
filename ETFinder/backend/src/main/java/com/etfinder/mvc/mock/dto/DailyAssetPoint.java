package com.etfinder.mvc.mock.dto;

import java.time.LocalDate;

//자산 추이 차트 포인트
public class DailyAssetPoint {

	private LocalDate baseDate;	//기준 일자 
	private Long totalAsset;	//총 자산 

	public DailyAssetPoint() {
	}

	public DailyAssetPoint(LocalDate baseDate, Long totalAsset) {
		this.baseDate = baseDate;
		this.totalAsset = totalAsset;
	}

	public LocalDate getBaseDate() {
		return baseDate;
	}

	public void setBaseDate(LocalDate baseDate) {
		this.baseDate = baseDate;
	}

	public Long getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Long totalAsset) {
		this.totalAsset = totalAsset;
	}

	@Override
	public String toString() {
		return "DailyAssetPoint [baseDate=" + baseDate + ", totalAsset=" + totalAsset + "]";
	}

}
