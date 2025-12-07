package com.etfinder.mvc.like.dto;

public class Like {
	
	private Long likeId;	//좋아요 ID
	private Long userId;	//유저 ID
	private Long etfId;		//etf ID
	
	public Like() {
	}

	public Like(Long likeId, Long userId, Long etfId) {
		this.likeId = likeId;
		this.userId = userId;
		this.etfId = etfId;
	}

	public Long getLikeId() {
		return likeId;
	}

	public void setLikeId(Long likeId) {
		this.likeId = likeId;
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

	@Override
	public String toString() {
		return "Like [likeId=" + likeId + ", userId=" + userId + ", etfId=" + etfId + "]";
	}

	
	
}
