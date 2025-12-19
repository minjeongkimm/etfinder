package com.etfinder.mvc.etf.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 모르는 필드 있어도 에러 안 나게 방지
public class KisPriceHistoryItem {

    @JsonProperty("stck_bsop_date")
    private String date; // 영업일자 (YYYYMMDD)

    @JsonProperty("stck_clpr")
    private String closePrice; // 종가

    @JsonProperty("stck_oprc")
    private String openPrice;  // 시가

    @JsonProperty("stck_hgpr")
    private String highPrice;  // 고가

    @JsonProperty("stck_lwpr")
    private String lowPrice;   // 저가

    @JsonProperty("acml_vol")
    private String volume;     // 누적 거래량
}