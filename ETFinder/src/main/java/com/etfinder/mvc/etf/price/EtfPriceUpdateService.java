package com.etfinder.mvc.etf.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.common.util.DateUtil;
import com.etfinder.mvc.etf.mapper.EtfMapper;

@Service
public class EtfPriceUpdateService {

    private static final Logger log = LoggerFactory.getLogger(EtfPriceUpdateService.class);

    private final KrxEtfApiClient krxEtfApiClient;
    private final EtfMapper etfMapper;

    public EtfPriceUpdateService(KrxEtfApiClient krxEtfApiClient, EtfMapper etfMapper) {
        this.krxEtfApiClient = krxEtfApiClient;
        this.etfMapper = etfMapper;
    }

    @Transactional
    public void updateTodayPrices() {

        // 1) 최근 영업일 basDd 찾기
        String basDd = DateUtil.findLatestBasDd(dd -> {
            KrxEtfPriceRes res = krxEtfApiClient.fetchDailyPrices(dd);
            return res != null
                    && res.getOutBlock1() != null
                    && !res.getOutBlock1().isEmpty();
        });

        log.info("Updating ETF current_price for basDd={}", basDd);

        // 2) KRX 호출
        KrxEtfPriceRes res = krxEtfApiClient.fetchDailyPrices(basDd);
        if (res == null || res.getOutBlock1() == null) {
            log.warn("No data from KRX for basDd={}", basDd);
            return;
        }

        int success = 0, failed = 0;

        // 3) 종목코드 매칭해서 종가로 current_price 업데이트
        for (KrxEtfPriceItem item : res.getOutBlock1()) {
            try {
                String etfCode = item.getIsuCd();                 // ISU_CD
                Integer currentPrice = parsePrice(item.getClosePrice()); // TDD_CLSPRC

                if (currentPrice == null) continue;

                etfMapper.updateCurrentPrice(etfCode, currentPrice);
                success++;

            } catch (Exception e) {
                failed++;
                log.error("Update failed code={}, msg={}", item.getIsuCd(), e.getMessage());
            }
        }

        log.info("ETF price update done. success={}, failed={}", success, failed);
    }

    private Integer parsePrice(String s) {
        if (s == null) return null;
        s = s.replace(",", "").trim();
        if (s.equals("-") || s.isEmpty()) return null; // '-' 인 경우 스킵
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
