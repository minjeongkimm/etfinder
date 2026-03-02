package com.etfinder.mvc.mock.dto;

import java.util.List;

//대시보드 통합 응답 DTO
public class DashboardResponse {
	
	private WalletResponse walletResponse;				//지갑 정보 
	private List<DailyAssetPoint> assetTrend;			//최근 7일/30일
	private List<MockHoldingResponse> holdings;			//보유 종목 
	private List<TradeHistoryResponse> recentTrades;	//최근 거래 내역
	private MockRankingResponse ranking;				//랭킹 정보 
	
	public DashboardResponse() {
	}

	public DashboardResponse(WalletResponse walletResponse, List<DailyAssetPoint> assetTrend,
			List<MockHoldingResponse> holdings, List<TradeHistoryResponse> recentTrades, MockRankingResponse ranking) {
		this.walletResponse = walletResponse;
		this.assetTrend = assetTrend;
		this.holdings = holdings;
		this.recentTrades = recentTrades;
		this.ranking = ranking;
	}

	public WalletResponse getWalletResponse() {
		return walletResponse;
	}

	public void setWalletResponse(WalletResponse walletResponse) {
		this.walletResponse = walletResponse;
	}

	public List<DailyAssetPoint> getAssetTrend() {
		return assetTrend;
	}

	public void setAssetTrend(List<DailyAssetPoint> assetTrend) {
		this.assetTrend = assetTrend;
	}

	public List<MockHoldingResponse> getHoldings() {
		return holdings;
	}

	public void setHoldings(List<MockHoldingResponse> holdings) {
		this.holdings = holdings;
	}

	public List<TradeHistoryResponse> getRecentTrades() {
		return recentTrades;
	}

	public void setRecentTrades(List<TradeHistoryResponse> recentTrades) {
		this.recentTrades = recentTrades;
	}

	public MockRankingResponse getRanking() {
		return ranking;
	}

	public void setRanking(MockRankingResponse ranking) {
		this.ranking = ranking;
	}

	@Override
	public String toString() {
		return "WalletDashboardResponse [walletResponse=" + walletResponse + ", assetTrend=" + assetTrend
				+ ", holdings=" + holdings + ", recentTrades=" + recentTrades + ", ranking=" + ranking + "]";
	}
	
	
}
