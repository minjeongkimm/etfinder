package com.etfinder.mvc.mock.dto;

//모의투자 랭킹
public class MockRanking {

	//유저 정보 
	private Long userId;		
	private String nickname;

	private Long totalAsset;	//총 자산 
	private Double returnRate; 	//초기 1,000 만원 대비 수익률 (%)
	private Integer rank;		//순위

	public MockRanking() {
	}

	public MockRanking(Long userId, String nickname, Long totalAsset, Double returnRate, Integer rank) {
		this.userId = userId;
		this.nickname = nickname;
		this.totalAsset = totalAsset;
		this.returnRate = returnRate;
		this.rank = rank;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Long totalAsset) {
		this.totalAsset = totalAsset;
	}

	public Double getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "MockRanking [userId=" + userId + ", nickname=" + nickname + ", totalAsset=" + totalAsset
				+ ", returnRate=" + returnRate + ", rank=" + rank + "]";
	}

}
