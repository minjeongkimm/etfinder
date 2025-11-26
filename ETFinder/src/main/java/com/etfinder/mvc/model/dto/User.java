package com.etfinder.mvc.model.dto;

import java.time.LocalDateTime;

public class User {
	private long userId;
	private String email;
	private String nickname;
	private int age;
	private String provider;
	private String providerId;
	private String propensity;
	private LocalDateTime createdAt;
	
	public User() {
	}

	public User(long userId, String email, String nickname, int age, String provider, String providerId,
			String propensity, LocalDateTime createdAt) {
		this.userId = userId;
		this.email = email;
		this.nickname = nickname;
		this.age = age;
		this.provider = provider;
		this.providerId = providerId;
		this.propensity = propensity;
		this.createdAt = createdAt;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getPropensity() {
		return propensity;
	}

	public void setPropensity(String propensity) {
		this.propensity = propensity;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", nickname=" + nickname + ", age=" + age + ", provider="
				+ provider + ", providerId=" + providerId + ", propensity=" + propensity + ", createdAt=" + createdAt
				+ "]";
	}
	
	
	
}
