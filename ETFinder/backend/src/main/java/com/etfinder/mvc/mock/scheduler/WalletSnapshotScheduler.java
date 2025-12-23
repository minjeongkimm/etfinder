package com.etfinder.mvc.mock.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.mock.dto.Wallet;
import com.etfinder.mvc.mock.mapper.WalletMapper;
import com.etfinder.mvc.mock.service.WalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 자산 스냅샷 자동 저장 Scheduler
 * 
 * - DEV: 매분 실행 (빠른 검증용, properties: 0 * * * * *)
 * - PROD: 매시간 실행 (properties: 0 1 * * * *)
 * - 모든 지갑 보유 사용자의 자산을 실시간 가격 기준으로 계산하여 스냅샷 저장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WalletSnapshotScheduler {

    private final WalletMapper walletMapper;
    private final WalletService walletService;

    // 스냅샷 활성화 여부
    @Value("${mock.snapshot.enabled:true}")
    private boolean snapshotEnabled;

    /**
     * 스냅샷 자동 저장 (설정 파일 기반 실행 주기)
     * 
     * application.properties 설정:
     * - DEV: scheduler.wallet-snapshot.cron=0 * * * * * (매분)
     * - PROD: scheduler.wallet-snapshot.cron=0 1 * * * * (매시간)
     */
    @Scheduled(cron = "${scheduler.wallet-snapshot.cron:0 1 * * * *}", zone = "Asia/Seoul")
    @Transactional
    public void saveSnapshots() {
        // 스냅샷 비활성화 체크
        if (!snapshotEnabled) {
            log.debug("⏭️ [스냅샷] 비활성화 (mock.snapshot.enabled=false)");
            return;
        }

        log.info("⏰ [자산 스냅샷] 시작");

        try {
            // 1. 모든 지갑 사용자 조회
            List<Wallet> wallets = walletMapper.selectAllWallets();

            if (wallets == null || wallets.isEmpty()) {
                log.info("📭 [자산 스냅샷] 대상 사용자 없음");
                return;
            }

            // 2. 각 사용자별 스냅샷 저장
            int successCount = 0;
            int failCount = 0;

            for (Wallet wallet : wallets) {
                try {
                    Long userId = wallet.getUserId();

                    // 현재 시각 스냅샷 저장 (분 단위 정밀도)
                    walletService.saveCurrentHourSnapshot(userId);

                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    log.error("❌ [자산 스냅샷] 저장 실패 (userId={}): {}", wallet.getUserId(), e.getMessage());
                }
            }

            log.info("✅ [자산 스냅샷] 완료 - 성공: {}/{}, 실패: {}",
                    successCount, wallets.size(), failCount);

        } catch (Exception e) {
            log.error("❌ [자산 스냅샷] 전체 프로세스 실패", e);
        }
    }
}
