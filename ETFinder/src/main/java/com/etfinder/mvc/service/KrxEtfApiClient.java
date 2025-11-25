package com.etfinder.mvc.service;

import com.etfinder.mvc.model.dto.KrxEtfPriceRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KrxEtfApiClient {

    private static final Logger log = LoggerFactory.getLogger(KrxEtfApiClient.class);

    @Value("${krx.api.key}")
    private String apiKey;

    // KRX 실제 엔드포인트
    private static final String URL =
            "https://data-dbg.krx.co.kr/svc/apis/etp/etf_bydd_trd";

    private final RestTemplate restTemplate = new RestTemplate();

    public KrxEtfPriceRes fetchDailyPrices(String basDd) {

        // 쿼리스트링
        String requestUrl = URL + "?basDd=" + basDd;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        headers.set("AUTH_KEY", apiKey);  // MUST

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        log.info("📡 Call KRX: {} (basDd={})", requestUrl, basDd);

        ResponseEntity<KrxEtfPriceRes> res =
                restTemplate.exchange(requestUrl, HttpMethod.GET, entity, KrxEtfPriceRes.class);

        KrxEtfPriceRes body = res.getBody();

        int size = (body != null && body.getOutBlock1() != null)
                ? body.getOutBlock1().size() : 0;

        log.info("📥 KRX response status={}, outBlockSize={}",
                res.getStatusCode(), size);

        return body;
    }
}
