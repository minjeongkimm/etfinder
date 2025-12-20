package com.etfinder.mvc.mock.service;

import java.time.LocalDate;
import java.util.List;

import com.etfinder.mvc.mock.dto.DailyAssetPoint;
import com.etfinder.mvc.mock.dto.DashboardResponse;
import com.etfinder.mvc.mock.dto.WalletResponse;

public interface WalletService {

    //1. 지갑 요약 데이터 조회 (wallet 없으면 자동 생성 후 반환)
    WalletResponse getWalletSummary(Long userId);

    //2. 지갑 잔액 + 총자산 리프레시 처리 (종목 평가금 반영하여 totalAsset 업데이트)
    int refreshTotalAsset(Long userId);

    //3. 가용 잔액 증감 처리
    int adjustBalance(Long userId, Long amountDiff);

    //4. 지갑 전체 초기화 (보유 종목, 지갑, 거래내역까지 초기화는 별도 처리 예상)
    int resetWallet(Long userId);

    //5. 자산 추이 조회 (대시보드 차트용)
    List<DailyAssetPoint> getAssetTrend(Long userId, LocalDate from, LocalDate to);

    //6. 대시보드 통합 데이터 제공
    DashboardResponse getDashboard(Long userId);
}
