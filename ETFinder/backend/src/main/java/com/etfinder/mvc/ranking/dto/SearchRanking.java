package com.etfinder.mvc.ranking.dto;

// 검색 기반 랭킹 dto
public class SearchRanking {

	private String keyword; // 검색 키워드
	private Long searchCount; // 검색 수

	public SearchRanking() {
	}

	public SearchRanking(String keyword, Long searchCount) {
		this.keyword = keyword;
		this.searchCount = searchCount;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;
	}

	private Integer rankChange; // 순위 변동 (null: NEW, 0: 변동없음, 양수: 상승, 음수: 하락)

	public Integer getRankChange() {
		return rankChange;
	}

	public void setRankChange(Integer rankChange) {
		this.rankChange = rankChange;
	}

	@Override
	public String toString() {
		return "SearchRanking [keyword=" + keyword + ", searchCount=" + searchCount + ", rankChange=" + rankChange
				+ "]";
	}

}
