package com.etfinder.mvc.ranking.dto;

// 검색 기반 랭킹 dto
public class SearchRanking {
	
	private String keyword;		//검색 키워드
	private Long searchCount;	//검색 수 
	
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

	@Override
	public String toString() {
		return "SearchRanking [keyword=" + keyword + ", searchCount=" + searchCount + "]";
	}

	

}
