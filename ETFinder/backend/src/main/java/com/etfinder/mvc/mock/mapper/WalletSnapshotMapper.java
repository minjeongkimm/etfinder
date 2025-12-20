package com.etfinder.mvc.mock.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.mock.dto.DailyAssetPoint;

@Mapper
public interface WalletSnapshotMapper {

    List<DailyAssetPoint> selectSnapshots(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
