package com.etfinder.mvc.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KrxEtfPriceItem {

    @JsonProperty("ISU_CD")      // 종목코드
    private String isuCd;

    @JsonProperty("TDD_CLSPRC")  // 종가(현재가로 사용)
    private String closePrice;

    public String getIsuCd() {
        return isuCd;
    }

    public void setIsuCd(String isuCd) {
        this.isuCd = isuCd;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }
}
