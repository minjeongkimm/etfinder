package com.etfinder.mvc.etf.history;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfDailyHistory;
import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;

@Service
public class EtfDailyHistoryUpdateService {
	
	private final KisPriceHistoryApiClient apiClient;
	private final EtfMapper etfMapper;
	
	public EtfDailyHistoryUpdateService(KisPriceHistoryApiClient apiClient, EtfMapper etfMapper) {
		this.apiClient = apiClient;
		this.etfMapper = etfMapper;
	}

	private static final Logger log = LoggerFactory.getLogger(EtfDailyHistoryUpdateService.class);
	private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	
    // [관리자용 메인] 모든 ETF의 1년치 과거 데이터를 적재
    public void loadAllEtfsHistory() {
        // 1. 토큰은 루프 밖에서 한 번만 발급 (재사용)
        String token = apiClient.getAccessToken();
        
        // 2. 전체 ETF 목록 조회 (DB 조회 1회)
        List<EtfProduct> allEtfs = etfMapper.selectAllEtf();
        
        log.info("총 {}개의 ETF에 대해 데이터 적재를 시작합니다.", allEtfs.size());

        for (EtfProduct etf : allEtfs) {
            try {
                // 개별 적재 메서드 호출
                loadHistoryData(token, etf);
                
                // 종목 간 너무 빠른 호출 방지 (0.2초)
                Thread.sleep(200); 
            } catch (Exception e) {
                log.error("{} 적재 중 오류 발생: {}", etf.getEtfName(), e.getMessage());
            }
        }
    }

    // [개별 적재 로직] 토큰과 ETF 객체를 파라미터로 받아서 조회 비용 절약
    @Transactional
    public void loadHistoryData(String token, EtfProduct etf) {
        String etfCode = etf.getEtfCode();

        // 날짜 설정 (오늘 ~ 1년 전)
        LocalDate endDate = LocalDate.now();
        LocalDate targetDate = endDate.minusYears(1); 

        String endDateStr = endDate.format(YYYYMMDD);
        String targetDateStr = targetDate.format(YYYYMMDD);

        log.info("Start loading history for {}: {} ~ {}", etfCode, targetDateStr, endDateStr);

        // 반복 호출 (API 100건 제한 극복)
        while (endDateStr.compareTo(targetDateStr) > 0) {
            
            // API 호출
            KisPriceHistoryRes res = apiClient.fetchDailyPriceFromKis(token, etfCode, targetDateStr, endDateStr);
            
            if (res == null || res.getItems() == null || res.getItems().isEmpty()) {
                log.warn("No data received for {}", etfCode);
                break; 
            }

            // DB 저장
            saveHistoryItems(etf, res.getItems());

            // 다음 루프 준비 (가장 과거 날짜의 '하루 전'으로 설정)
            List<KisPriceHistoryItem> items = res.getItems();
            String oldestDateStr = items.get(items.size() - 1).getDate(); 
            
            LocalDate oldestDate = LocalDate.parse(oldestDateStr, YYYYMMDD);
            endDateStr = oldestDate.minusDays(1).format(YYYYMMDD); 

            // API 연속 호출 방지 (0.1초)
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
        
        log.info("History loading finished for {}", etfCode);
    }

    // 리스트 데이터를 DB에 저장
    private void saveHistoryItems(EtfProduct etf, List<KisPriceHistoryItem> items) {
        int savedCount = 0;

        for (KisPriceHistoryItem item : items) {
            LocalDate baseDate = LocalDate.parse(item.getDate(), YYYYMMDD);

            // 중복 체크
            int count = etfMapper.countHistoryByDate(etf.getEtfId(), baseDate);
            if (count > 0) continue;

            // 매핑
            EtfDailyHistory history = new EtfDailyHistory();
            history.setEtfId(etf.getEtfId());
            history.setBaseDate(baseDate);
            
            history.setClosePrice(parseInt(item.getClosePrice()));
            history.setOpenPrice(parseInt(item.getOpenPrice()));
            history.setHighPrice(parseInt(item.getHighPrice()));
            history.setLowPrice(parseInt(item.getLowPrice()));
            history.setVolume(parseLong(item.getVolume()));

            // 저장
            etfMapper.insertDailyHistory(history);
            savedCount++;
        }
        log.debug("Saved {} rows for {}", savedCount, etf.getEtfName());
    }

    // 파싱 헬퍼 메서드
    private Integer parseInt(String s) {
        if (s == null) return null;
        s = s.replace(",", "").trim();
        if (s.isEmpty() || s.equals("-")) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Long parseLong(String s) {
        if (s == null) return null;
        s = s.replace(",", "").trim();
        if (s.isEmpty() || s.equals("-")) return null;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
