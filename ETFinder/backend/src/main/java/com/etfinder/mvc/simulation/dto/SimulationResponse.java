package com.etfinder.mvc.simulation.dto;

import java.util.List;

public class SimulationResponse {
	private Long totalInvestment;      // 원금
    private Long totalProfit;          // 수익금 (얼마 벌었는지)
    private Double totalReturnRate;     // 최종 수익률 (%)
    private Long finalAmount;          // 최종 평가 금액 (원금 + 수익금)
    private Integer etfCount;          // 투자한 ETF 개수
    private List<SimulationDetail> details;     // 포함된 ETF 정보
    
    public SimulationResponse() {
	}

	public SimulationResponse(Long totalInvestment, Long totalProfit, Double totalReturnRate, Long finalAmount,
			Integer etfCount, List<SimulationDetail> details) {
		super();
		this.totalInvestment = totalInvestment;
		this.totalProfit = totalProfit;
		this.totalReturnRate = totalReturnRate;
		this.finalAmount = finalAmount;
		this.etfCount = etfCount;
		this.details = details;
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

	public Double getTotalReturnRate() {
		return totalReturnRate;
	}

	public void setTotalReturnRate(Double totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
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

	public List<SimulationDetail> getDetails() {
		return details;
	}

	public void setDetails(List<SimulationDetail> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "SimulationResponse [totalInvestment=" + totalInvestment + ", totalProfit=" + totalProfit
				+ ", totalReturnRate=" + totalReturnRate + ", finalAmount=" + finalAmount + ", etfCount=" + etfCount
				+ ", details=" + details + "]";
	}

	public static class SimulationDetail {
		private String etfName;
		private Integer ratio;
		private Double returnRate1y;
		private long profit;
		
		public SimulationDetail() {
		}

		public SimulationDetail(String etfName, Integer ratio, Double returnRate1y, long profit) {
			super();
			this.etfName = etfName;
			this.ratio = ratio;
			this.returnRate1y = returnRate1y;
			this.profit = profit;
		}

		public String getEtfName() {
			return etfName;
		}

		public void setEtfName(String etfName) {
			this.etfName = etfName;
		}

		public Integer getRatio() {
			return ratio;
		}

		public void setRatio(Integer ratio) {
			this.ratio = ratio;
		}

		public Double getReturnRate1y() {
			return returnRate1y;
		}

		public void setReturnRate1y(Double returnRate1y) {
			this.returnRate1y = returnRate1y;
		}

		public long getProfit() {
			return profit;
		}

		public void setProfit(long profit) {
			this.profit = profit;
		}

		@Override
		public String toString() {
			return "SimulationDetail [etfName=" + etfName + ", ratio=" + ratio + ", returnRate1y=" + returnRate1y
					+ ", profit=" + profit + "]";
		}
		
	}
    
}
