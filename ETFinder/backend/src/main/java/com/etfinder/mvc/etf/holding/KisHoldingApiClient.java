package com.etfinder.mvc.etf.holding;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KisHoldingApiClient {

    private static final Logger log = LoggerFactory.getLogger(KisHoldingApiClient.class);

    @Value("${kis.appkey}")
    private String appKey;

    @Value("${kis.appsecret}")
    private String appSecret;

    // 한국투자증권 제공 기본 엔드포인트
    private static final String BASE_URL = "https://openapi.koreainvestment.com:9443";
    
    private final RestTemplate restTemplate;
    
    // 타임아웃 설정
    public KisHoldingApiClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 연결 거는데 3초 (3000ms)
        factory.setReadTimeout(5000);    // 데이터 읽는데 5초 (5000ms)
        
        this.restTemplate = new RestTemplate(factory);
    }

    // 1. 토큰 발급 (POST)
    public String getAccessToken() {
        String url = BASE_URL + "/oauth2/tokenP";
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appKey);
        body.put("appsecret", appSecret);

        try {
            Map response = restTemplate.postForObject(url, body, Map.class);
            if (response != null && response.containsKey("access_token")) {
                return "Bearer " + response.get("access_token");
            }
        } catch (Exception e) {
            log.error("Token issue failed: {}", e.getMessage());
        }
        throw new RuntimeException("Failed to get Access Token");
    }

    // 2. 구성 종목 조회 (GET)
    public KisHoldingRes fetchEtfHoldings(String token, String etfCode) {
        String url = BASE_URL + "/uapi/etfetn/v1/quotations/inquire-component-stock-price";

        String requestUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", etfCode)
                .queryParam("FID_COND_SCR_DIV_CODE", "11216")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", token);
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", "FHKST121600C0");
        headers.set("custtype", "P");

        try {
            ResponseEntity<KisHoldingRes> res = 
                restTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(headers), KisHoldingRes.class);
            return res.getBody();
        } catch (Exception e) {
            log.error("API Call failed for {}: {}", etfCode, e.getMessage());
            return null;
        }
    }
}