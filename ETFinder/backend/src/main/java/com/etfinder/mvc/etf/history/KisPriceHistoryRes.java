package com.etfinder.mvc.etf.history;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisPriceHistoryRes {

    @JsonProperty("rt_cd")
    private String rtCd; // 성공 실패 여부 (0: 성공)

    @JsonProperty("msg1")
    private String msg1; // 응답 메시지

    @JsonProperty("output2") 
    private List<KisPriceHistoryItem> items; // 과거 데이터 리스트
}