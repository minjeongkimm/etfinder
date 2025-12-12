package com.etfinder.mvc.etf.dto;

public class EtfAiDescriptionResponse {

	private String summary;			// 한줄요약 -> "미국 우량 기술주 10개에 집중 투자하는 ETF"
	private String description;		// 상세 설명 -> "이 ETF는... 적합합니다."
	private String growthLevel;		// 성장성 수준 (매우 높음 / 높음 / 보통 / 낮음)
	private String dividendLevel;	// 배당 수준 (매우 높음 / 높음 / 보통 / 낮음 / 없음)
	private String recommendTag;	// 추천 대상 태그 -> #공격형 #장기투자
	
	public EtfAiDescriptionResponse() {
	}

	public EtfAiDescriptionResponse(String summary, String description, String growthLevel, String dividendLevel,
			String recommendTag) {
		super();
		this.summary = summary;
		this.description = description;
		this.growthLevel = growthLevel;
		this.dividendLevel = dividendLevel;
		this.recommendTag = recommendTag;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGrowthLevel() {
		return growthLevel;
	}

	public void setGrowthLevel(String growthLevel) {
		this.growthLevel = growthLevel;
	}

	public String getDividendLevel() {
		return dividendLevel;
	}

	public void setDividendLevel(String dividendLevel) {
		this.dividendLevel = dividendLevel;
	}

	public String getRecommendTag() {
		return recommendTag;
	}

	public void setRecommendTag(String recommendTag) {
		this.recommendTag = recommendTag;
	}

	@Override
	public String toString() {
		return "EtfAiDescriptionResponse [summary=" + summary + ", description=" + description + ", growthLevel="
				+ growthLevel + ", dividendLevel=" + dividendLevel + ", recommendTag=" + recommendTag + "]";
	}
	
}
