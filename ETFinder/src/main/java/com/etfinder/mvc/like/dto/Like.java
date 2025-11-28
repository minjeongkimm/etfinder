package com.etfinder.mvc.like.dto;

public class Like {
	
	private Long like_id;
	private Long user_id;
	private Long etf_id;
	
	public Like() {
	}

	public Like(Long like_id, Long user_id, Long etf_id) {
		this.like_id = like_id;
		this.user_id = user_id;
		this.etf_id = etf_id;
	}

	public Long getLike_id() {
		return like_id;
	}

	public void setLike_id(Long like_id) {
		this.like_id = like_id;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Long getEtf_id() {
		return etf_id;
	}

	public void setEtf_id(Long etf_id) {
		this.etf_id = etf_id;
	}

	@Override
	public String toString() {
		return "Like [like_id=" + like_id + ", user_id=" + user_id + ", etf_id=" + etf_id + "]";
	}
	
	
	
}
