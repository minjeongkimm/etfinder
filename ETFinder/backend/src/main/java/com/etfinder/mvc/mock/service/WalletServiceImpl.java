package com.etfinder.mvc.mock.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.mock.dto.DailyAssetPoint;
import com.etfinder.mvc.mock.dto.DashboardResponse;
import com.etfinder.mvc.mock.dto.MinuteAssetPoint;
import com.etfinder.mvc.mock.dto.MockHolding;
import com.etfinder.mvc.mock.dto.Wallet;
import com.etfinder.mvc.mock.dto.WalletResponse;
import com.etfinder.mvc.mock.mapper.MockHoldingMapper;
import com.etfinder.mvc.mock.mapper.TradeHistoryMapper;
import com.etfinder.mvc.mock.mapper.WalletMapper;
import com.etfinder.mvc.mock.mapper.WalletSnapshotMapper;
import com.etfinder.mvc.realtime.cache.RealTimePriceCache;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletMapper walletMapper;
    private final MockHoldingMapper holdingMapper;
    private final TradeHistoryMapper tradeMapper;
    private final WalletSnapshotMapper snapshotMapper;
    private final MockRankingService rankingService;
    private final RealTimePriceCache priceCache; // Added field
    private final EtfMapper etfMapper; // Added field

    public WalletServiceImpl(
            WalletMapper walletMapper,
            MockHoldingMapper holdingMapper,
            TradeHistoryMapper tradeMapper,
            WalletSnapshotMapper snapshotMapper,
            MockRankingService rankingService,
            RealTimePriceCache priceCache, // Added parameter
            EtfMapper etfMapper) { // Added parameter

        this.walletMapper = walletMapper;
        this.holdingMapper = holdingMapper;
        this.tradeMapper = tradeMapper;
        this.snapshotMapper = snapshotMapper;
        this.rankingService = rankingService;
        this.priceCache = priceCache; // Assigned field
        this.etfMapper = etfMapper; // Assigned field
    }

    /**
     * 1. 지갑 요약 데이터 조회
     * - 지갑이 없으면 초기 지갑을 생성한 뒤 조회
     */
    @Override
    @Transactional // ✅ INSERT 가능성 있으므로 readOnly=false
    public WalletResponse getWalletSummary(Long userId) {

        Wallet wallet = walletMapper.selectByUserId(userId);

        // 최초 지갑 없으면 생성 로직 포함
        if (wallet == null) {
            walletMapper.insertInitialWallet(userId);
            wallet = walletMapper.selectByUserId(userId);
        }

        // cashRatio와 stockRatio 계산
        long balance = wallet.getBalance();
        long totalAsset = wallet.getTotalAsset();

        Long cashRatio = 0L;
        Long stockRatio = 0L;

        if (totalAsset > 0) {
            cashRatio = (balance * 100) / totalAsset;
            stockRatio = 100 - cashRatio;
        }

        // 실현손익 조회 (최신 스냅샷에서)
        Long realizedProfit = snapshotMapper.selectLatestRealizedProfit(userId);
        if (realizedProfit == null) {
            realizedProfit = 0L;
        }

        WalletResponse response = new WalletResponse();
        response.setBalance(balance);
        response.setTotalAsset(totalAsset);
        response.setRealizedProfit(realizedProfit);
        response.setCashRatio(cashRatio);
        response.setStockRatio(stockRatio);

        return response;
    }

    /**
     * 2. 지갑 잔액 + 총자산 리프레시
     */
    @Override
    @Transactional // 총자산 update 발생
    public int refreshTotalAsset(Long userId) {

        // 보유 종목 조회
        var holdings = holdingMapper.selectHoldingView(userId);

        long evalAsset = 0L;

        if (holdings != null) {
            evalAsset = holdings.stream()
                    .mapToLong(h -> h.getEvalAmount())
                    .sum();
        }

        Wallet w = walletMapper.selectByUserId(userId);
        if (w == null)
            return 0;

        long total = w.getBalance() + evalAsset;

        return walletMapper.updateTotalAsset(userId, total);
    }

    /**
     * 3. 가용 잔액 증감 처리
     */
    @Override
    @Transactional
    public int adjustBalance(Long userId, Long amountDiff) {

        Wallet wallet = walletMapper.selectByUserId(userId);

        if (wallet == null) {
            walletMapper.insertInitialWallet(userId);
            wallet = walletMapper.selectByUserId(userId);
        }

        long newBalance = wallet.getBalance() + amountDiff;

        if (newBalance < 0) {
            throw new IllegalStateException("잔액 부족");
        }

        return walletMapper.updateBalance(userId, newBalance);
    }

    /**
     * 4. 지갑 초기화 (보유 종목, 거래 내역, 지갑 삭제 후 재생성)
     */
    @Override
    @Transactional
    public int resetWallet(Long userId) {

        // 1. 보유 종목 전체 삭제
        holdingMapper.deleteAllByUserId(userId);

        // 2. 거래 내역 전체 삭제
        tradeMapper.deleteAllByUserId(userId);

        // 3. 지갑 삭제
        walletMapper.deleteByUserId(userId);

        // 4. 초기 지갑 재생성
        int result = walletMapper.insertInitialWallet(userId);

        // 5. 초기 상태 스냅샷 저장 (신규)
        if (result > 0) {
            saveCurrentHourSnapshot(userId);
        }

        return result;
    }

    /**
     * 5. 자산 추이 조회 (조회 전용)
     */
    @Override
    @Transactional(readOnly = true) // ✅ 조회만
    public List<DailyAssetPoint> getAssetTrend(Long userId, LocalDate from, LocalDate to) {
        return snapshotMapper.selectSnapshots(userId, from, to);
    }

    /**
     * 6. 대시보드 통합 정보 조회
     */
    @Override
    @Transactional // ✅ getWalletSummary 안에서 INSERT 가능성 있으므로 readOnly=false
    public DashboardResponse getDashboard(Long userId) {

        WalletResponse wallet = getWalletSummary(userId);

        // 최근 30일 자산 추이 (일별 집계 데이터)
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(30);
        List<DailyAssetPoint> assetTrend = snapshotMapper.selectDailyAggregated(userId, from, to);

        // Fallback: 스냅샷이 없으면 현재 시각 스냅샷 생성
        if (assetTrend == null || assetTrend.isEmpty()) {
            saveCurrentHourSnapshot(userId);
            assetTrend = snapshotMapper.selectDailyAggregated(userId, from, to);
        }

        return new DashboardResponse(
                wallet,
                assetTrend,
                holdingMapper.selectHoldingView(userId),
                tradeMapper.selectRecentTrades(userId),
                rankingService.getRankingInfo(userId) // 랭킹 정보 함께 반환
        );
    }

    /**
     * 7. 실시간 가격 기반 총 자산 계산
     */
    @Override
    @Transactional(readOnly = true)
    public Long computeTotalAssetRealtime(Long userId) {

        // 1. 지갑 잔액 조회
        Wallet wallet = walletMapper.selectByUserId(userId);
        if (wallet == null) {
            return 0L;
        }
        Long balance = wallet.getBalance();

        // 2. 보유 종목 조회 (종목코드 포함)
        List<MockHolding> holdings = holdingMapper.selectByUserId(userId);
        if (holdings == null || holdings.isEmpty()) {
            return balance;
        }

        // 3. 평가금 계산 (실시간 가격 우선)
        long evalAsset = 0L;
        for (MockHolding holding : holdings) {
            String stockCode = holding.getStockCode();
            Integer quantity = holding.getQuantity();

            // 실시간 캐시에서 가격 조회
            Integer price = priceCache.getPrice(stockCode)
                    .orElseGet(() -> {
                        // Fallback: DB에서 current_price 조회
                        EtfProduct etf = etfMapper.selectOneEtf(holding.getEtfId());
                        return etf != null ? etf.getCurrentPrice() : 0;
                    });

            evalAsset += (long) quantity * price;
        }

        return balance + evalAsset;
    }

    /**
     * 8. 현재 시각의 분 단위 스냅샷 저장
     */
    @Override
    @Transactional
    public void saveCurrentHourSnapshot(Long userId) {
        // 분 단위 정밀도 (초/나노초만 0으로)
        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);

        // 실시간 가격 기반 총 자산 계산
        Long totalAsset = computeTotalAssetRealtime(userId);

        // 현재 누적 실현손익 조회
        Long realizedProfit = snapshotMapper.selectLatestRealizedProfit(userId);
        if (realizedProfit == null) {
            realizedProfit = 0L;
        }

        // 스냅샷 upsert (UNIQUE KEY: user_id, base_datetime)
        // 같은 분에 여러 번 호출되어도 UPDATE만 수행됨
        snapshotMapper.upsertHourlySnapshot(userId, now, totalAsset, realizedProfit);
    }

    /**
     * 8-1. 현재 시각의 분 단위 스냅샷 저장 (실현손익 누적)
     */
    @Override
    @Transactional
    public void saveCurrentHourSnapshotWithRealizedPnL(Long userId, long realizedPnLDelta) {
        // 분 단위 정밀도 (초/나노초만 0으로)
        LocalDateTime now = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);

        // 실시간 가격 기반 총 자산 계산
        Long totalAsset = computeTotalAssetRealtime(userId);

        // 현재 누적 실현손익 조회 및 증분 추가
        Long currentRealized = snapshotMapper.selectLatestRealizedProfit(userId);
        if (currentRealized == null) {
            currentRealized = 0L;
        }
        Long newRealizedProfit = currentRealized + realizedPnLDelta;

        // 스냅샷 upsert (UNIQUE KEY: user_id, base_datetime)
        // 같은 분에 여러 번 호출되어도 UPDATE만 수행됨
        snapshotMapper.upsertHourlySnapshot(userId, now, totalAsset, newRealizedProfit);
    }

    /**
     * 9. 분 단위 자산 추이 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<MinuteAssetPoint> getMinuteTrend(Long userId, int minutes) {
        List<MinuteAssetPoint> trend = snapshotMapper.selectMinuteTrend(userId, minutes);

        // null 체크 (빈 리스트 반환)
        if (trend == null) {
            return List.of();
        }

        return trend;
    }
}
