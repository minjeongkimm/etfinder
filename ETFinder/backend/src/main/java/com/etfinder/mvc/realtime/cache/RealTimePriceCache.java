package com.etfinder.mvc.realtime.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket을 통해 수신한 실시간 ETF 가격을 서버 메모리에 캐싱
 * 
 * 용도:
 * - 시간별 자산 스냅샷 계산 시 실시간 가격 사용
 * - 캐시가 없으면 DB의 current_price로 fallback
 * 
 * Thread-safe: ConcurrentHashMap 사용
 */
@Slf4j
@Component
public class RealTimePriceCache {

    // 종목코드(String) -> 현재가(Integer) 매핑
    // Key: ETF 종목코드 (예: "152100", "360750")
    // Value: 실시간 현재가 (원 단위)
    private final ConcurrentHashMap<String, Integer> priceCache = new ConcurrentHashMap<>();

    /**
     * WebSocket에서 실시간 가격 수신 시 호출
     * 
     * @param stockCode ETF 종목코드 (예: "152100")
     * @param price     현재가 (원)
     */
    public void updatePrice(String stockCode, Integer price) {
        if (stockCode == null || price == null || price <= 0) {
            log.warn("⚠️ [가격 캐시] 유효하지 않은 데이터: stockCode={}, price={}", stockCode, price);
            return;
        }

        priceCache.put(stockCode, price);
        log.debug("💸 [가격 캐시 업데이트] {} = {}원 (캐시 크기: {})", stockCode, price, priceCache.size());
    }

    /**
     * 특정 종목의 캐시된 가격 조회
     * 
     * @param stockCode ETF 종목코드
     * @return 캐시된 가격 (없으면 Optional.empty)
     */
    public Optional<Integer> getPrice(String stockCode) {
        Integer price = priceCache.get(stockCode);
        return Optional.ofNullable(price);
    }

    /**
     * 현재 캐시된 종목 수 조회 (모니터링용)
     */
    public int getCacheSize() {
        return priceCache.size();
    }

    /**
     * 캐시 전체 삭제 (테스트/관리 용도)
     */
    public void clearAll() {
        int prevSize = priceCache.size();
        priceCache.clear();
        log.info("🗑️ [가격 캐시] 전체 삭제 완료 (이전 크기: {})", prevSize);
    }
}
