package com.etfinder.mvc.mock.dto;

import java.time.LocalDateTime;

/**
 * 실현손익 DTO
 * 매도 거래 시 계산된 실현손익 정보를 담는 객체
 */
public class RealizedPnL {

    private Long tradeId; // 거래 ID (trade_history 참조)
    private Long userId; // 사용자 ID
    private Long etfId; // ETF ID
    private String etfName; // ETF 종목명
    private Integer soldQuantity; // 매도 수량
    private Integer avgCost; // 평단가 (매입 평균 단가)
    private Integer sellPrice; // 매도 단가
    private Long realizedPnL; // 실현손익 = (매도가 - 평단가) * 수량
    private LocalDateTime createdAt;// 거래 시각

    public RealizedPnL() {
    }

    public RealizedPnL(Long tradeId, Long userId, Long etfId, String etfName, Integer soldQuantity, Integer avgCost,
            Integer sellPrice, Long realizedPnL, LocalDateTime createdAt) {
        this.tradeId = tradeId;
        this.userId = userId;
        this.etfId = etfId;
        this.etfName = etfName;
        this.soldQuantity = soldQuantity;
        this.avgCost = avgCost;
        this.sellPrice = sellPrice;
        this.realizedPnL = realizedPnL;
        this.createdAt = createdAt;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
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

    public String getEtfName() {
        return etfName;
    }

    public void setEtfName(String etfName) {
        this.etfName = etfName;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(Integer avgCost) {
        this.avgCost = avgCost;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getRealizedPnL() {
        return realizedPnL;
    }

    public void setRealizedPnL(Long realizedPnL) {
        this.realizedPnL = realizedPnL;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RealizedPnL [tradeId=" + tradeId + ", userId=" + userId + ", etfId=" + etfId + ", etfName=" + etfName
                + ", soldQuantity=" + soldQuantity + ", avgCost=" + avgCost + ", sellPrice=" + sellPrice
                + ", realizedPnL=" + realizedPnL + ", createdAt=" + createdAt + "]";
    }
}
