package com.etfinder.mvc.user.dto;

public class UserResponse {

	private String email;
	private String nickname;
	private int age;
	private String propensity;
	
	public UserResponse() {
	}

	public UserResponse(String email, String nickname, int age, String propensity) {
		super();
		this.email = email;
		this.nickname = nickname;
		this.age = age;
		this.propensity = propensity;
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

	@Override
	public String toString() {
		return "UserResponse [email=" + email + ", nickname=" + nickname + ", age=" + age + ", propensity=" + propensity
				+ "]";
	}
	
}
