package com.etfinder.mvc.model.dto;

import java.time.LocalDateTime;

public class Bookmark {
	private Long bookmarkId;
	private Long userId;
	private Long etfId;
	private LocalDateTime createdAt;
	
	public Bookmark() {
	}

	public Bookmark(Long bookmarkId, Long userId, Long etfId, LocalDateTime createdAt) {
		this.bookmarkId = bookmarkId;
		this.userId = userId;
		this.etfId = etfId;
		this.createdAt = createdAt;
	}

	public Long getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(Long bookmarkId) {
		this.bookmarkId = bookmarkId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Bookmark [bookmarkId=" + bookmarkId + ", userId=" + userId + ", etfId=" + etfId + ", createdAt="
				+ createdAt + "]";
	}

	
}
