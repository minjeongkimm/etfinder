package com.etfinder.mvc.etf.holding;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfHolding;
import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.mapper.EtfMapper;

@Service
public class EtfHoldingUpdateService {

    private static final Logger log = LoggerFactory.getLogger(EtfHoldingUpdateService.class);

    private final KisHoldingApiClient apiClient;
    private final EtfMapper etfMapper;

    public EtfHoldingUpdateService(KisHoldingApiClient apiClient, EtfMapper etfMapper) {
        this.apiClient = apiClient;
        this.etfMapper = etfMapper;
    }

    public void updateAllEtfHoldings() {
        log.info("Starting daily ETF Holdings update...");

        // 1. 토큰 발급
        String token = apiClient.getAccessToken();

        // 2. 전체 ETF 가져오기
        List<EtfProduct> etfList = etfMapper.selectAllEtf(); 
        
        // ETF가 아예 없는 경우 체크
        if (etfList.isEmpty()) {
            log.warn("저장된 ETF가 하나도 없습니다! etf_product 테이블을 확인해주세요.");
            return;
        }
        log.info("총 {}개의 ETF 업데이트 예정.", etfList.size());

        // 진행 상황 체크용
        int success = 0;
        int count = 0; 

        for (EtfProduct etf : etfList) {
        	count++;
            try {
            	
            	// 10개마다 로그 찍어서 진행률 확인
                if (count % 10 == 0) {
                    log.info("[진행중] {} / {} - 현재: {}", count, etfList.size(), etf.getEtfName());
                }
            	
                // 3. API 호출
                KisHoldingRes res = apiClient.fetchEtfHoldings(token, etf.getEtfCode());
                if (res == null || res.getHoldings() == null) continue;

                // 4. 개별 삭제/저장
                saveHoldingsForEtf(etf, res.getHoldings());
                success++;
                
                // 5. 1초 휴식
                Thread.sleep(1000);

            } catch (Exception e) {
                log.error("Failed to update {}: {}", etf.getEtfName(), e.getMessage());
            }
        }
        log.info("Update Complete. Success: {}", success);
    }
    
    // 한 etf 단위로 트랜잭션 수행
    @Transactional
    public void saveHoldingsForEtf(EtfProduct etf, List<KisHoldingItem> items) {
    	
    	// 기존 보유종목 삭제 (Reset)
    	etfMapper.deleteHoldingsByEtfId(etf.getEtfId());

        // 새 데이터 저장
        for (KisHoldingItem item : items) {
        	
        	EtfHolding holding = new EtfHolding();
        	
        	holding.setEtfId(etf.getEtfId());
        	holding.setStockCode(item.getStockCode());
        	holding.setStockName(item.getStockName());
        	holding.setWeight(new BigDecimal(item.getWeight()));
        	holding.setStockPrice(Integer.parseInt(item.getPrice()));
        	
            etfMapper.insertHolding(holding);
        }
    }
}