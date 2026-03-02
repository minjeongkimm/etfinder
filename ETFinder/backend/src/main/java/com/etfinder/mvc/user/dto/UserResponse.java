package com.etfinder.mvc.user.dto;

import java.time.LocalDateTime;

public class UserResponse {

	private String email;
	private String nickname;
	private int age;
	private String propensity;
	private String providerId;
	private LocalDateTime createdAt;

	public UserResponse() {
	}

	public UserResponse(String email, String nickname, int age, String propensity, String providerId,
			LocalDateTime createdAt) {
		this.email = email;
		this.nickname = nickname;
		this.age = age;
		this.propensity = propensity;
		this.providerId = providerId;
		this.createdAt = createdAt;
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

	public String getPropensity() {
		return propensity;
	}

	public void setPropensity(String propensity) {
		this.propensity = propensity;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "UserResponse [email=" + email + ", nickname=" + nickname + ", age=" + age + ", propensity=" + propensity
				+ ", providerId=" + providerId + ", createdAt=" + createdAt + "]";
	}

}
