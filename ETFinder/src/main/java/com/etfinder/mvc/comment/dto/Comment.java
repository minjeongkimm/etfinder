package com.etfinder.mvc.comment.dto;

import java.time.LocalDateTime;

public class Comment {

	private Long commentId;
	private Long userId;
	private Long etfId;
	private String content;
	private String sentiment;
	private LocalDateTime createdAt;
	
	public Comment() {
	}

	public Comment(Long commentId, Long userId, Long etfId, String content, String sentiment, LocalDateTime createdAt) {
		this.commentId = commentId;
		this.userId = userId;
		this.etfId = etfId;
		this.content = content;
		this.sentiment = sentiment;
		this.createdAt = createdAt;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", userId=" + userId + ", etfId=" + etfId + ", content=" + content
				+ ", sentiment=" + sentiment + ", createdAt=" + createdAt + "]";
	}

	
	
}
