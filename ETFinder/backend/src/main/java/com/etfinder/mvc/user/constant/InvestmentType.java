package com.etfinder.mvc.user.constant;

// 성향 테스트 결과를 3가지로 고정해놓는 enum
public enum InvestmentType {

	STABLE("안정형", 0, 90),
	NEUTRAL("중립형", 91, 140),
	AGGRESSIVE("공격형", 141, Integer.MAX_VALUE);
	
	private final String label;
	private final int minScore;
	private final int maxScore;
	
	private InvestmentType(String label, int minScore, int maxScore) {
		this.label = label;
		this.minScore = minScore;
		this.maxScore = maxScore;
	}

	public static InvestmentType findByScore(int score) {
		// 받은 점수가 범위 안에 있으면 해당 타입 리턴
		for(InvestmentType type : InvestmentType.values()) {
			if(score >= type.minScore && score <= type.maxScore)
				return type;
		}
		// 오류날 경우 안정형 리턴
		return STABLE;
	}

	public String getLabel() {
		return label;
	}

	public int getMinScore() {
		return minScore;
	}

	public int getMaxScore() {
		return maxScore;
	}

}
