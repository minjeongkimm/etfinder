package com.etfinder.mvc.ranking.dto;

import java.time.LocalDateTime;

// 조회수 랭킹 dto
public class ViewRanking {

	private Long statId;
	private Long etfId;
	private String etfName;
	private Long viewCount;
	private LocalDateTime updatedAt;

	public ViewRanking() {
	}

	public ViewRanking(Long statId, Long etfId, String etfName, Long viewCount, LocalDateTime updatedAt) {
		this.statId = statId;
		this.etfId = etfId;
		this.etfName = etfName;
		this.viewCount = viewCount;
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

	public String getEtfName() {
		return etfName;
	}

	public void setEtfName(String etfName) {
		this.etfName = etfName;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "ViewRanking [statId=" + statId + ", etfId=" + etfId + ", etfName=" + etfName + ", viewCount="
				+ viewCount + ", updatedAt=" + updatedAt + "]";
	}

}
