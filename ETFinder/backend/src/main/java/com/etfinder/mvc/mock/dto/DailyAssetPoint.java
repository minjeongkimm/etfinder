package com.etfinder.mvc.mock.dto;

import java.time.LocalDate;

//자산 추이 차트 포인트
public class DailyAssetPoint {

	private LocalDate baseDate; 	// 기준 일자
	private Long totalAsset; 		// 총 자산(잔액 + 평가금)
	private Long realizedProfit; 	// 누적 실현 손익 (기준 일자까지 확정된 손익)

	public DailyAssetPoint() {
	}

	public DailyAssetPoint(LocalDate baseDate, Long totalAsset, Long realizedProfit) {
		this.baseDate = baseDate;
		this.totalAsset = totalAsset;
		this.realizedProfit = realizedProfit;
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

	public Long getRealizedProfit() {
		return realizedProfit;
	}

	public void setRealizedProfit(Long realizedProfit) {
		this.realizedProfit = realizedProfit;
	}

	@Override
	public String toString() {
		return "DailyAssetPoint [baseDate=" + baseDate + ", totalAsset=" + totalAsset + ", realizedProfit="
				+ realizedProfit + "]";
	}

}
