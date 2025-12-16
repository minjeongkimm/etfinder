package com.etfinder.mvc.etf.holding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisHoldingItem {

    @JsonProperty("stck_shrn_iscd") // API가 주는 이름
    private String stockCode;       // 자바에서 사용할 이름

    @JsonProperty("hts_kor_isnm")
    private String stockName;

    @JsonProperty("etf_cnfg_issu_rlim") // 비중
    private String weight;

    @JsonProperty("stck_prpr")      // 현재가
    private String price;
}