package com.etfinder.mvc.simulation.dto;

import java.util.List;

public class SimulationResponse {
	private Long totalInvestment;      // 원금
    private Long totalProfit;          // 수익금 (얼마 벌었는지)
    private Double totalYieldRate;     // 최종 수익률 (%)
    private Long finalAmount;          // 최종 평가 금액 (원금 + 수익금)
    private Integer etfCount;          // 투자한 ETF 개수
    private List<String> etfNames;     // 포함된 ETF 이름들
    
    public SimulationResponse() {
	}

	public SimulationResponse(Long totalInvestment, Long totalProfit, Double totalYieldRate, Long finalAmount,
			Integer etfCount, List<String> etfNames) {
		super();
		this.totalInvestment = totalInvestment;
		this.totalProfit = totalProfit;
		this.totalYieldRate = totalYieldRate;
		this.finalAmount = finalAmount;
		this.etfCount = etfCount;
		this.etfNames = etfNames;
	}

	public Long getTotalInvestment() {
		return totalInvestment;
	}

	public void setTotalInvestment(Long totalInvestment) {
		this.totalInvestment = totalInvestment;
	}

	public Long getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Long totalProfit) {
		this.totalProfit = totalProfit;
	}

	public Double getTotalYieldRate() {
		return totalYieldRate;
	}

	public void setTotalYieldRate(Double totalYieldRate) {
		this.totalYieldRate = totalYieldRate;
	}

	public Long getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(Long finalAmount) {
		this.finalAmount = finalAmount;
	}

	public Integer getEtfCount() {
		return etfCount;
	}

	public void setEtfCount(Integer etfCount) {
		this.etfCount = etfCount;
	}

	public List<String> getEtfNames() {
		return etfNames;
	}

	public void setEtfNames(List<String> etfNames) {
		this.etfNames = etfNames;
	}

	@Override
	public String toString() {
		return "SimulationResponse [totalInvestment=" + totalInvestment + ", totalProfit=" + totalProfit
				+ ", totalYieldRate=" + totalYieldRate + ", finalAmount=" + finalAmount + ", etfCount=" + etfCount
				+ ", etfNames=" + etfNames + "]";
	}
    
}
