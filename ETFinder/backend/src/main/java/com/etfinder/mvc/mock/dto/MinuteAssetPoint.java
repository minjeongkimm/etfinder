package com.etfinder.mvc.mock.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 분 단위 자산 스냅샷 DTO
 * 
 * 프론트엔드 차트에 표시할 분 단위 데이터
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MinuteAssetPoint {

    /**
     * 스냅샷 시각 (분 단위)
     */
    private LocalDateTime baseDatetime;

    /**
     * 총 자산
     */
    private Long totalAsset;
}
