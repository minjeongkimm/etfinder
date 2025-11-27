package com.etfinder.mvc.user.dto;

import com.etfinder.mvc.user.constant.InvestmentType;

// 백엔드에서 점수계산 후 결과를 프론트로 넘겨주는 용도
public class PropensityResult {

	private String type;	// 프론트 로직용 영문명(AGGRESIVE)
	private String label;	// 화면 출력용 한글명(공격형)
	
	public PropensityResult() {
	}

	public PropensityResult(InvestmentType investmentType) {
		super();
		this.type = investmentType.name();
		this.label = investmentType.getLabel();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "PropensityResult [type=" + type + ", label=" + label + "]";
	}
}
