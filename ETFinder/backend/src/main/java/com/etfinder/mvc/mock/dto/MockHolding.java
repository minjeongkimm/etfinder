package com.etfinder.mvc.mock.dto;

import java.time.LocalDateTime;

// 모의투자 보유 종목 엔티티 (DB holdings 테이블 매핑)
public class MockHolding {

    private Long holdingId; // 보유 레코드 PK
    private Long userId; // 보유한 사용자 ID
    private Long etfId; // 보유 ETF ID
    private Integer quantity; // 보유 수량
    private Integer averagePrice; // 매입 평균 단가
    private LocalDateTime createdAt; // 최초 생성 시각
    private LocalDateTime updatedAt; // 최근 수정 시각

    private String stockCode; // ETF 종목코드 (JOIN 시 사용, 실시간 가격 캐시 조회용)
    private Long evalCost; // 총 매입 금액 (averagePrice * quantity)
    private Long evalProfit; // 종목별 누적 수익 (실현 + 평가 손익)

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

    public Long getEvalCost() {
        return evalCost;
    }

    public void setEvalCost(Long evalCost) {
        this.evalCost = evalCost;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Long getEvalProfit() {
        return evalProfit;
    }

    public void setEvalProfit(Long evalProfit) {
        this.evalProfit = evalProfit;
    }

    @Override
    public String toString() {
        return "MockHolding [holdingId=" + holdingId + ", userId=" + userId + ", etfId=" + etfId
                + ", quantity=" + quantity + ", averagePrice=" + averagePrice + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", evalCost=" + evalCost + ", evalProfit=" + evalProfit + "]";
    }
}
