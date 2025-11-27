package com.etfinder.mvc.user.dto;

public class UserUpdateRequest {

	private String nickname;
	private String email;
	private Integer age;
	private String propensity;
	
	public UserUpdateRequest() {
	}

	public UserUpdateRequest(String nickname, String email, int age, String propensity) {
		super();
		this.nickname = nickname;
		this.email = email;
		this.age = age;
		this.propensity = propensity;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return "UserUpdateRequest [nickname=" + nickname + ", email=" + email + ", age=" + age + ", propensity="
				+ propensity + "]";
	}

}
