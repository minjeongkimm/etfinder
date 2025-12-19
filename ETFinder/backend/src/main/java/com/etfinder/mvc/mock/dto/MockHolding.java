package com.etfinder.mvc.mock.dto;

import java.time.LocalDateTime;

//모의투자 보유 종목 dto
public class MockHolding {

	private Long holdingId;
	private Long userId;
	private Long etfId;
	private Integer quantity;
	private Integer averagePrice;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public MockHolding() {
	}

	public MockHolding(Long holdingId, Long userId, Long etfId, Integer quantity, Integer averagePrice,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.holdingId = holdingId;
		this.userId = userId;
		this.etfId = etfId;
		this.quantity = quantity;
		this.averagePrice = averagePrice;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(Long holdingId) {
		this.holdingId = holdingId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEtfId() {
		return etfId;
	}

	public void setEtfId(Long etfId) {
		this.etfId = etfId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(Integer averagePrice) {
		this.averagePrice = averagePrice;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "MockHolding [holdingId=" + holdingId + ", userId=" + userId + ", etfId=" + etfId + ", quantity="
				+ quantity + ", averagePrice=" + averagePrice + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ "]";
	}

	
}
