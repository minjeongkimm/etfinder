package com.etfinder.mvc.user.dto;

public class PropensityAiResponse {

	// 프론트에서 꾸미기 쉽게 하기 위해 내용을 나눔
	private String title;		// 제목 - 한줄 요약
	private String reason;		// 그렇게 생각하는 이유
	private String advice;		// 조언 
	private String cheering;	// 응원의 한마디 
	
	public PropensityAiResponse() {
	}

	public PropensityAiResponse(String title, String reason, String advice, String cheering) {
		super();
		this.title = title;
		this.reason = reason;
		this.advice = advice;
		this.cheering = cheering;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getCheering() {
		return cheering;
	}

	public void setCheering(String cheering) {
		this.cheering = cheering;
	}

	@Override
	public String toString() {
		return "PropensityAiResponse [title=" + title + ", reason=" + reason + ", advice=" + advice + ", cheering="
				+ cheering + "]";
	}
	
}
