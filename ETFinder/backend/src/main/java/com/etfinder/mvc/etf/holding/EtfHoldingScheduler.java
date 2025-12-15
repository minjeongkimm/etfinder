package com.etfinder.mvc.etf.holding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EtfHoldingScheduler {

    private static final Logger log = LoggerFactory.getLogger(EtfHoldingScheduler.class);
    private final EtfHoldingUpdateService service;

    public EtfHoldingScheduler(EtfHoldingUpdateService service) {
        this.service = service;
    }

    // 매일 저녁 7시 30분
    @Scheduled(cron = "0 10 19 * * *", zone = "Asia/Seoul")
    public void runHoldingUpdate() {
        log.info("Triggering Daily Holdings Update...");
        service.updateAllEtfHoldings();
    }
}