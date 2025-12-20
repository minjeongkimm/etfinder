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
@Transactional
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

    @Override
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
        response.setRealizedProfit(0L); // 실현 손익 계산 기능 구현 예정
        response.setCashRatio(cashRatio);
        response.setStockRatio(stockRatio);
        
        return response;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<DailyAssetPoint> getAssetTrend(Long userId, LocalDate from, LocalDate to) {

        return snapshotMapper.selectSnapshots(userId, from, to);
    }

    @Override
    @Transactional(readOnly = true)
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
