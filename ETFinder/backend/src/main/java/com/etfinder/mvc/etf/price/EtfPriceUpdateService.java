package com.etfinder.mvc.etf.price;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.common.util.DateUtil;
import com.etfinder.mvc.etf.dto.EtfDailyHistory;
import com.etfinder.mvc.etf.dto.EtfProduct;
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
        
        // [성능 최적화] 반복문 돌기 전에 전체 ETF 엔티티를 미리 조회해서 Map에 담기
        // (반복문 안에서 매번 SELECT 하면 DB 부하가 심함)
        Map<String, EtfProduct> etfMap = etfMapper.selectAllEtf().stream()
                .collect(Collectors.toMap(EtfProduct::getEtfCode, Function.identity()));
        
        // 기준 날짜 파싱 (String -> LocalDate)
        LocalDate baseDate = LocalDate.parse(basDd, DateTimeFormatter.ofPattern("yyyyMMdd"));

        int success = 0, failed = 0;

        // 3) 종목코드 매칭해서 종가로 current_price 업데이트
        for (KrxEtfPriceItem item : res.getOutBlock1()) {
            try {
                String etfCode = item.getIsuCd();                 // ISU_CD
                Integer currentPrice = parsePrice(item.getClosePrice()); // TDD_CLSPRC

                if (currentPrice == null) continue;
                etfMapper.updateCurrentPrice(etfCode, currentPrice);
                
                // --- 과거 시세 테이블에 INSERT ---
                // Map에서 Entity 찾기 (DB 조회 X, 메모리 조회 O -> 빠름)
                EtfProduct etf = etfMap.get(etfCode);
                
                // 우리 DB에 있는 ETF인 경우에만 저장
                if (etf != null) {
                    saveHistory(etf, baseDate, item, currentPrice);
                }
                success++;

            } catch (Exception e) {
                failed++;
                log.error("Update failed code={}, msg={}", item.getIsuCd(), e.getMessage());
            }
        }

        log.info("ETF price update done. success={}, failed={}", success, failed);
    }
	
	private void saveHistory(EtfProduct etf, LocalDate baseDate, KrxEtfPriceItem item, Integer closePrice) {
        
		// 1. 중복 체크
	    int count = etfMapper.countHistoryByDate(etf.getEtfId(), baseDate);
	    if (count > 0) {
	        return;
	    }

	    // 2. DTO 생성
	    EtfDailyHistory history = new EtfDailyHistory();
	    history.setEtfId(etf.getEtfId()); 
	    history.setBaseDate(baseDate);
	    history.setClosePrice(closePrice);

	    // 3. 저장
	    etfMapper.insertDailyHistory(history);
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
