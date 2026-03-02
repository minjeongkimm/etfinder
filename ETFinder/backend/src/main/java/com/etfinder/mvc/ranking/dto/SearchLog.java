package com.etfinder.mvc.ranking.dto;

import java.time.LocalDateTime;

public class SearchLog {

	private Long searchLogId;
	private Long userId;
	private String keyword;
	private LocalDateTime createdAt;

	public SearchLog() {
	}

	public SearchLog(Long searchLogId, Long userId, String keyword, LocalDateTime createdAt) {
		this.searchLogId = searchLogId;
		this.userId = userId;
		this.keyword = keyword;
		this.createdAt = createdAt;
	}

	public Long getSearchLogId() {
		return searchLogId;
	}

	public void setSearchLogId(Long searchLogId) {
		this.searchLogId = searchLogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "SearchLog [searchLogId=" + searchLogId + ", userId=" + userId + ", keyword=" + keyword + ", createdAt="
				+ createdAt + "]";
	}

}
