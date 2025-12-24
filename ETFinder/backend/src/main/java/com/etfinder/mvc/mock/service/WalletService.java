package com.etfinder.mvc.mock.service;

import java.time.LocalDate;
import java.util.List;

import com.etfinder.mvc.mock.dto.DailyAssetPoint;
import com.etfinder.mvc.mock.dto.DashboardResponse;
import com.etfinder.mvc.mock.dto.MinuteAssetPoint;
import com.etfinder.mvc.mock.dto.WalletResponse;

public interface WalletService {

    // 1. 지갑 요약 데이터 조회 (wallet 없으면 자동 생성 후 반환)
    WalletResponse getWalletSummary(Long userId);

    // 2. 지갑 잔액 + 총자산 리프레시 처리 (종목 평가금 반영하여 totalAsset 업데이트)
    int refreshTotalAsset(Long userId);

    // 3. 가용 잔액 증감 처리
    int adjustBalance(Long userId, Long amountDiff);

    // 4. 지갑 전체 초기화 (보유 종목, 지갑, 거래내역까지 초기화는 별도 처리 예상)
    int resetWallet(Long userId);

    // 5. 자산 추이 조회 (대시보드 차트용)
    List<DailyAssetPoint> getAssetTrend(Long userId, LocalDate from, LocalDate to);

    /**
     * 6. 대시보드 통합 정보 조회
     */
    DashboardResponse getDashboard(Long userId);

    /**
     * 7. 실시간 가격 기반 총 자산 계산
     * - WebSocket 캐시 우선, fallback으로 DB current_price 사용
     */
    Long computeTotalAssetRealtime(Long userId);

    /**
     * 8. 현재 시각의 정각 스냅샷 저장
     * - 거래/리셋 시 호출되어 당시간 스냅샷 upsert
     */
    void saveCurrentHourSnapshot(Long userId);

    /**
     * 8-1. 현재 시각의 정각 스냅샷 저장 (실현손익 포함)
     * - 매도 시 호출되어 실현손익을 누적하여 저장
     * 
     * @param userId           사용자 ID
     * @param realizedPnLDelta 이번 거래의 실현손익 (누적에 더해질 값)
     */
    void saveCurrentHourSnapshotWithRealizedPnL(Long userId, long realizedPnLDelta);

    /**
     * 9. 분 단위 자산 추이 조회 (차트용)
     * - 최근 N분간의 스냅샷 반환
     * 
     * @param userId  사용자 ID
     * @param minutes 조회 범위 (분)
     * @return 분 단위 스냅샷 리스트
     */
    List<MinuteAssetPoint> getMinuteTrend(Long userId, int minutes);
}
