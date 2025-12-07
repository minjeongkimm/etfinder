package com.etfinder.mvc.etf.dto;

// 추천 로직 이후 도출된 결과를 받아서 프론트로 보내는 용도
public class EtfRecommendResponse {
	
	// db에 들어있는 기본 정보
	private Long etfId;
	private String etfCode;
	private String etfName;
	private String market;
	private String theme;
	private Double fee;
	private Integer riskRating;
	private long aum;
	private Double return1yr;
	
	// 추천 로직 이후 도출된 점수
	private Double score;
	
	public EtfRecommendResponse() {
	}

	public EtfRecommendResponse(EtfProduct etf, Double score) {
		this.etfId = etf.getEtfId();
		this.etfCode = etf.getEtfCode();
		this.etfName = etf.getEtfName();
		this.market = etf.getMarket();
		this.theme = etf.getTheme();
		this.fee = etf.getFee();
		this.riskRating = etf.getRiskRating();
		this.aum = etf.getAum();
		this.return1yr = etf.getReturn1yr();
		this.score = score;
	}
	
	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public String getEtfCode() {
		return etfCode;
	}

	public void setEtfCode(String etfCode) {
		this.etfCode = etfCode;
	}

	public String getEtfName() {
		return etfName;
	}

	public void setEtfName(String etfName) {
		this.etfName = etfName;
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

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Integer getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(Integer riskRating) {
		this.riskRating = riskRating;
	}

	public long getAum() {
		return aum;
	}

	public void setAum(long aum) {
		this.aum = aum;
	}

	public Double getReturn1yr() {
		return return1yr;
	}

	public void setReturn1yr(Double return1yr) {
		this.return1yr = return1yr;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "EtfRecommendResponse [etfId=" + etfId + ", etfCode=" + etfCode + ", etfName=" + etfName + ", market="
				+ market + ", theme=" + theme + ", fee=" + fee + ", riskRating=" + riskRating + ", aum=" + aum
				+ ", return1yr=" + return1yr + ", score=" + score + "]";
	}
	
}
