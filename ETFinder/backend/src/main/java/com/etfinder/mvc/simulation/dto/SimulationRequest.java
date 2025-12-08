package com.etfinder.mvc.simulation.dto;

import java.util.List;

public class SimulationRequest {
	
	private Long investmentAmount; //총 투자금
	private List<PortfolioItem> portfolio; //포폴에 넣을 etf 정보
	
	public SimulationRequest() {
	}
	
	public SimulationRequest(Long investmentAmount, List<PortfolioItem> portfolio) {
		super();
		this.investmentAmount = investmentAmount;
		this.portfolio = portfolio;
	}

	public Long getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(Long investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

	public List<PortfolioItem> getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(List<PortfolioItem> portfolio) {
		this.portfolio = portfolio;
	}

	@Override
	public String toString() {
		return "SimulationRequest [investmentAmonut=" + investmentAmount + ", portfolio=" + portfolio + "]";
	}

	public static class PortfolioItem {
		private Long etfId;		//etf id
		private Integer ratio;	//etf의 비중
		
		public PortfolioItem() {
		}
		
		public PortfolioItem(Long etfId, Integer ratio) {
			super();
			this.etfId = etfId;
			this.ratio = ratio;
		}

		public Long getEtfId() {
			return etfId;
		}

		public void setEtfId(Long etfId) {
			this.etfId = etfId;
		}

		public Integer getRatio() {
			return ratio;
		}

		public void setRatio(Integer ratio) {
			this.ratio = ratio;
		}

		@Override
		public String toString() {
			return "PortfolioItem [etfId=" + etfId + ", ratio=" + ratio + "]";
		}
	}
}
