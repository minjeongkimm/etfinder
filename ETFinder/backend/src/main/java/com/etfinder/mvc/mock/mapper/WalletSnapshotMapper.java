package com.etfinder.mvc.mock.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.DailyAssetPoint;
import com.etfinder.mvc.mock.dto.MinuteAssetPoint;

@Mapper
public interface WalletSnapshotMapper {

        /**
         * 시간별 스냅샷 조회 (내부용, DATETIME 기준)
         */
        List<DailyAssetPoint> selectSnapshots(
                        @Param("userId") Long userId,
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

        /**
         * 시간별 스냅샷 Upsert
         * - base_datetime 기준으로 INSERT or UPDATE
         * - 매시간 1개씩 저장
         */
        int upsertHourlySnapshot(
                        @Param("userId") Long userId,
                        @Param("baseDatetime") LocalDateTime baseDatetime,
                        @Param("totalAsset") Long totalAsset,
                        @Param("realizedProfit") Long realizedProfit);

        /**
         * 일별 집계 스냅샷 조회 (대시보드용)
         * - 각 날짜의 마지막 시간 스냅샷만 반환
         * - LocalDate 형태로 반환 (프론트엔드 호환성)
         */
        List<DailyAssetPoint> selectDailyAggregated(
                        @Param("userId") Long userId,
                        @Param("from") LocalDate from,
                        @Param("to") LocalDate to);

        /**
         * 분 단위 자산 추이 조회 (차트용)
         * - 최근 N분간의 스냅샷 반환
         * - 시간 순서대로 정렬 (오름차순)
         */
        List<MinuteAssetPoint> selectMinuteTrend(
                        @Param("userId") Long userId,
                        @Param("minutes") int minutes);
}
