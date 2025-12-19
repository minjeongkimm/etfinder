package com.etfinder.mvc.etf.history;

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
public class KisPriceHistoryApiClient {
	
    private static final Logger log = LoggerFactory.getLogger(KisPriceHistoryApiClient.class);

	@Value("${kis.appkey}")
    private String appKey;

    @Value("${kis.appsecret}")
    private String appSecret;
    
    // 한국투자증권 제공 기본 엔드포인트
    private static final String BASE_URL = "https://openapi.koreainvestment.com:9443";
    
    private final RestTemplate restTemplate;
    
    // 타임아웃 설정
    public KisPriceHistoryApiClient() {
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
    
    // 2. 과거 시세 조회 (GET)
    public KisPriceHistoryRes fetchDailyPriceFromKis(String accessToken, String etfCode, String startDate, String endDate) {
        
        // 1. URL 및 쿼리 파라미터 설정
        String url = BASE_URL + "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")      // 주식/ETF
                .queryParam("FID_INPUT_ISCD", etfCode)          // 종목코드
                .queryParam("FID_INPUT_DATE_1", startDate)      // 시작일 (YYYYMMDD)
                .queryParam("FID_INPUT_DATE_2", endDate)        // 종료일 (YYYYMMDD)
                .queryParam("FID_PERIOD_DIV_CODE", "D")         // 일봉
                .queryParam("FID_ORG_ADJ_PRC", "0");            // 수정주가 반영(0)

        // 2. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", accessToken); // 토큰
        headers.set("appkey", appKey);                   // 앱키
        headers.set("appsecret", appSecret);             // 시크릿
        headers.set("tr_id", "FHKST03010100");                 // ★ 거래 ID 필수
        headers.set("custtype", "P");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // 3. API 호출
        try {
            ResponseEntity<KisPriceHistoryRes> response = this.restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    KisPriceHistoryRes.class
            );

            log.info("KIS API 호출 성공: ETF={}, 기간={}~{}", etfCode, startDate, endDate);
            return response.getBody();

        } catch (Exception e) {
            log.error("KIS API 호출 실패: {}", e.getMessage());
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }
}
