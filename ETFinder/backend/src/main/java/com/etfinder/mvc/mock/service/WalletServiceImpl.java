package com.etfinder.mvc.mock.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.mock.dto.DailyAssetPoint;
import com.etfinder.mvc.mock.dto.DashboardResponse;
import com.etfinder.mvc.mock.dto.Wallet;
import com.etfinder.mvc.mock.dto.WalletResponse;
import com.etfinder.mvc.mock.mapper.MockHoldingMapper;
import com.etfinder.mvc.mock.mapper.TradeHistoryMapper;
import com.etfinder.mvc.mock.mapper.WalletMapper;
import com.etfinder.mvc.mock.mapper.WalletSnapshotMapper;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletMapper walletMapper;
    private final MockHoldingMapper holdingMapper;
    private final TradeHistoryMapper tradeMapper;
    private final WalletSnapshotMapper snapshotMapper;
    private final MockRankingService rankingService;

    public WalletServiceImpl(
            WalletMapper walletMapper,
            MockHoldingMapper holdingMapper,
            TradeHistoryMapper tradeMapper,
            WalletSnapshotMapper snapshotMapper,
            MockRankingService rankingService) {

        this.walletMapper = walletMapper;
        this.holdingMapper = holdingMapper;
        this.tradeMapper = tradeMapper;
        this.snapshotMapper = snapshotMapper;
        this.rankingService = rankingService;
    }

    /**
     * 1. 지갑 요약 데이터 조회
     *    - 지갑이 없으면 초기 지갑을 생성한 뒤 조회
     */
    @Override
    @Transactional   // ✅ INSERT 가능성 있으므로 readOnly=false
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
        
        WalletResponse response = new WalletResponse();
        response.setBalance(balance);
        response.setTotalAsset(totalAsset);
        response.setRealizedProfit(0L); // TODO: 실현 손익 계산 기능 구현 예정
        response.setCashRatio(cashRatio);
        response.setStockRatio(stockRatio);
        
        return response;
    }

    /**
     * 2. 지갑 잔액 + 총자산 리프레시
     */
    @Override
    @Transactional   // 총자산 update 발생
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
        if (w == null) return 0;

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
        return walletMapper.insertInitialWallet(userId);
    }

    /**
     * 5. 자산 추이 조회 (조회 전용)
     */
    @Override
    @Transactional(readOnly = true)   // ✅ 조회만
    public List<DailyAssetPoint> getAssetTrend(Long userId, LocalDate from, LocalDate to) {
        return snapshotMapper.selectSnapshots(userId, from, to);
    }

    /**
     * 6. 대시보드 통합 정보 조회
     */
    @Override
    @Transactional   // ✅ getWalletSummary 안에서 INSERT 가능성 있으므로 readOnly=false
    public DashboardResponse getDashboard(Long userId) {

        WalletResponse wallet = getWalletSummary(userId);
        
        // 최근 30일 자산 추이
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(30);
        List<DailyAssetPoint> assetTrend = snapshotMapper.selectSnapshots(userId, from, to);

        return new DashboardResponse(
                wallet,
                assetTrend,
                holdingMapper.selectHoldingView(userId),
                tradeMapper.selectRecentTrades(userId),
                rankingService.getRankingInfo(userId)  // 랭킹 정보 함께 반환
        );
    }
}
