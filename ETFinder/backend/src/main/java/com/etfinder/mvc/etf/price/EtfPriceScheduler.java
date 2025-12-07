package com.etfinder.mvc.etf.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EtfPriceScheduler {

    private static final Logger log = LoggerFactory.getLogger(EtfPriceScheduler.class);

    private final EtfPriceUpdateService service;

    public EtfPriceScheduler(EtfPriceUpdateService service) {
        this.service = service;
    }

    // 매일 19:00 KST
    @Scheduled(cron = "0 0 19 * * *", zone = "Asia/Seoul")
//    @Scheduled(cron = "0 21 17 * * *", zone = "Asia/Seoul")
    public void runDailyUpdate() {
        log.info("Running daily ETF price update job...");
        service.updateTodayPrices();
    }
}
