package com.etfinder.mvc.etf.dto;

public class SearchCondition {

	// 검색용
	private String keyword; // ETF명/코드 부분 검색
	private String market; // KOR / USA
	private String theme; // 테마

	// 수수료 범위
	private Double minFee;
	private Double maxFee;

	// 시가총액 범위
	private Long minAum;
	private Long maxAum;

	// 위험등급
	private Integer riskRating;

	// 정렬
	private String orderBy; // 정렬 기준 컬럼명
	private String orderDir; // ASC / DESC

	// 기본 생성자, getter/setter
	public SearchCondition() {
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Double getMinFee() {
		return minFee;
	}

	public void setMinFee(Double minFee) {
		this.minFee = minFee;
	}

	public Double getMaxFee() {
		return maxFee;
	}

	public void setMaxFee(Double maxFee) {
		this.maxFee = maxFee;
	}

	public Long getMinAum() {
		return minAum;
	}

	public void setMinAum(Long minAum) {
		this.minAum = minAum;
	}

	public Long getMaxAum() {
		return maxAum;
	}

	public void setMaxAum(Long maxAum) {
		this.maxAum = maxAum;
	}

	public Integer getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(Integer riskRating) {
		this.riskRating = riskRating;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(String orderDir) {
		this.orderDir = orderDir;
	}

	@Override
	public String toString() {
		return "SearchCondition [keyword=" + keyword + ", market=" + market + ", theme=" + theme + ", minFee=" + minFee
				+ ", maxFee=" + maxFee + ", minAum=" + minAum + ", maxAum=" + maxAum + ", riskRating=" + riskRating
				+ ", orderBy=" + orderBy + ", orderDir=" + orderDir + "]";
	}

}
