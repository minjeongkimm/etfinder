package com.etfinder.mvc.ranking.dto;

import java.time.LocalDateTime;


// 조회수, 댓글 기반 랭킹 dto
public class EtfRanking {

	private Long statId;
	private Long etfId;
	private Long viewCount;
	private Long commentCount;
	private LocalDateTime updatedAt;

	public EtfRanking() {
	}

	public EtfRanking(Long statId, Long etfId, Long viewCount, Long commentCount, LocalDateTime updatedAt) {
		this.statId = statId;
		this.etfId = etfId;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.updatedAt = updatedAt;
	}

	public Long getStatId() {
		return statId;
	}

	public void setStatId(Long statId) {
		this.statId = statId;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Ranking [statId=" + statId + ", etfId=" + etfId + ", viewCount=" + viewCount + ", commentCount="
				+ commentCount + ", updatedAt=" + updatedAt + "]";
	}

}
