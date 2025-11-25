package com.etfinder.mvc.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KrxEtfPriceRes {

    @JsonProperty("OutBlock_1")   // JSON의 "OutBlock_1" 배열에 매핑
    private List<KrxEtfPriceItem> outBlock1;

    public List<KrxEtfPriceItem> getOutBlock1() {
        return outBlock1;
    }

    public void setOutBlock1(List<KrxEtfPriceItem> outBlock1) {
        this.outBlock1 = outBlock1;
    }
}
