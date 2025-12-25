package com.etfinder.mvc.etf.holding;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisHoldingRes {

    @JsonProperty("rt_cd")
    private String rtCd; // 성공여부 (0: 성공)

    @JsonProperty("output2") // 구성종목 리스트
    private List<KisHoldingItem> holdings;
}