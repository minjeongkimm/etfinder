package com.etfinder.mvc.user.dto;

import java.util.List;

//프론트에서 테스트 후 백엔드로 값 보내주는 용도
public class PropensityRequest {

	// 선택한 답들의 리스트
	private List<Integer> answers;
	
	public PropensityRequest() {
	}

	public PropensityRequest(List<Integer> answers) {
		super();
		this.answers = answers;
	}

	public List<Integer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Integer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "PropensityRequest [answers=" + answers + "]";
	}
	
}
