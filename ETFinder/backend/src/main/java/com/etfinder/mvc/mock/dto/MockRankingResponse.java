package com.etfinder.mvc.mock.dto;

import java.util.List;

//모의투자 랭킹 응답용 DTO
public class MockRankingResponse {

	private Integer myRank;				//유저 순위
	private Integer totalUsers;			//총 유저 수 
	private Double topPercent;			//ex) 5.0 -> Top 5%
	private List<MockRanking> topUsers;	//상위 랭킹 유저 
	
	public MockRankingResponse() {
	}

	public MockRankingResponse(Integer myRank, Integer totalUsers, Double topPercent, List<MockRanking> topUsers) {
		this.myRank = myRank;
		this.totalUsers = totalUsers;
		this.topPercent = topPercent;
		this.topUsers = topUsers;
	}

	public Integer getMyRank() {
		return myRank;
	}

	public void setMyRank(Integer myRank) {
		this.myRank = myRank;
	}

	public Integer getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	public Double getTopPercent() {
		return topPercent;
	}

	public void setTopPercent(Double topPercent) {
		this.topPercent = topPercent;
	}

	public List<MockRanking> getTopUsers() {
		return topUsers;
	}

	public void setTopUsers(List<MockRanking> topUsers) {
		this.topUsers = topUsers;
	}

	@Override
	public String toString() {
		return "MockRankingResponse [myRank=" + myRank + ", totalUsers=" + totalUsers + ", topPercent=" + topPercent
				+ ", topUsers=" + topUsers + "]";
	}
	
	
}
