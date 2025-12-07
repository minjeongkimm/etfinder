package com.etfinder.mvc.etf.dto;

import java.time.LocalDateTime;

public class EtfProduct {
	private Long etfId;
	private String etfCode;
	private String etfName;

	private String market;
	private String theme;

	private Double fee;
	private Integer riskRating;
	private Long aum;
	private Integer currentPrice;
	private Double return1mo;
	private Double return3mo;
	private Double return6mo;
	private Double return1yr;
	private Double return3yr;
	private String description;
	private LocalDateTime createdAt;
	
	// 좋아요 기능 관련
	private Integer likeCount;	// 좋아요 수
	private Boolean likedByMe = false;	// 사용자 본인이 눌렀는지 여부


	public EtfProduct() {
	}

	public EtfProduct(Long etfId, String etfCode, String etfName, String market, String theme, Double fee,
			Integer riskRating, Long aum, Integer currentPrice, Double return1mo, Double return3mo, Double return6mo,
			Double return1yr, Double return3yr, String description, LocalDateTime createdAt, Integer likeCount) {
		this.etfId = etfId;
		this.etfCode = etfCode;
		this.etfName = etfName;
		this.market = market;
		this.theme = theme;
		this.fee = fee;
		this.riskRating = riskRating;
		this.aum = aum;
		this.currentPrice = currentPrice;
		this.return1mo = return1mo;
		this.return3mo = return3mo;
		this.return6mo = return6mo;
		this.return1yr = return1yr;
		this.return3yr = return3yr;
		this.description = description;
		this.createdAt = createdAt;
		this.likeCount = likeCount;
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

	public Long getAum() {
		return aum;
	}

	public void setAum(Long aum) {
		this.aum = aum;
	}

	public Integer getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Integer currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Double getReturn1mo() {
		return return1mo;
	}

	public void setReturn1mo(Double return1mo) {
		this.return1mo = return1mo;
	}

	public Double getReturn3mo() {
		return return3mo;
	}

	public void setReturn3mo(Double return3mo) {
		this.return3mo = return3mo;
	}

	public Double getReturn6mo() {
		return return6mo;
	}

	public void setReturn6mo(Double return6mo) {
		this.return6mo = return6mo;
	}

	public Double getReturn1yr() {
		return return1yr;
	}

	public void setReturn1yr(Double return1yr) {
		this.return1yr = return1yr;
	}

	public Double getReturn3yr() {
		return return3yr;
	}

	public void setReturn3yr(Double return3yr) {
		this.return3yr = return3yr;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	
	

	public Boolean getLikedByMe() {
		return likedByMe;
	}

	public void setLikedByMe(Boolean likedByMe) {
		this.likedByMe = likedByMe;
	}

	@Override
	public String toString() {
		return "EtfProduct [etfId=" + etfId + ", etfCode=" + etfCode + ", etfName=" + etfName + ", market=" + market
				+ ", theme=" + theme + ", fee=" + fee + ", riskRating=" + riskRating + ", aum=" + aum
				+ ", currentPrice=" + currentPrice + ", return1mo=" + return1mo + ", return3mo=" + return3mo
				+ ", return6mo=" + return6mo + ", return1yr=" + return1yr + ", return3yr=" + return3yr
				+ ", description=" + description + ", createdAt=" + createdAt + ", likeCount=" + likeCount
				+ ", likedByMe=" + likedByMe + "]";
	}



}
