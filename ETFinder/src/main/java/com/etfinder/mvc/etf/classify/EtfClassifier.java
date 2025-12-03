package com.etfinder.mvc.etf.classify;

public class EtfClassifier {

	/**
     * ============================================
     * 1. 시장(Market) 분류 (업그레이드 버전)
     * - KOR (국내)
     * - USA (미국)
     * - GLOBAL (그 외 해외: 중국, 인도, 일본 등)
     * ============================================
     */
    public static String extractMarket(String name) {
        if (name == null) return "KOR"; // 기본값

        String n = name.toUpperCase().replace(" ", "");

        // 1️⃣ USA (미국) - 가장 확실한 키워드
        if (n.contains("미국") || n.contains("US") || n.contains("USA")
                || n.contains("NASDAQ") || n.contains("나스닥")
                || n.contains("S&P") || n.contains("SP500")
                || n.contains("다우") || n.contains("DOW")
                || n.contains("FANG") || n.contains("채권") && n.contains("달러")) { // 달러채권도 미국으로
            return "USA";
        }

        // 2️⃣ GLOBAL (미국 제외 해외) - 구체적인 국가명 체크
        if (n.contains("중국") || n.contains("CHINA") || n.contains("차이나")
                || n.contains("홍콩") || n.contains("HONGKONG") || n.contains("항셍")
                || n.contains("인도") || n.contains("INDIA") || n.contains("NIFTY")
                || n.contains("일본") || n.contains("JAPAN") || n.contains("NIKKEI") || n.contains("엔선물")
                || n.contains("베트남") || n.contains("VIETNAM")
                || n.contains("유럽") || n.contains("EURO") || n.contains("유로") || n.contains("독일")
                || n.contains("라틴") || n.contains("브라질")
                || n.contains("글로벌") || n.contains("GLOBAL") || n.contains("선진국") || n.contains("신흥국")) {
            return "GLOBAL";
        }

        // 3️⃣ KOR (국내) - 나머지는 다 국내로 간주
        // KODEX, TIGER 같은 브랜드가 붙어있으면서 위 해외 키워드가 없으면 국내 주식임
        return "KOR";
    }

    /**
     * ============================================
     * 2. 테마(Theme) 분류 [대폭 업그레이드 🔥]
     * - 위에서부터 순서대로 걸러지니, 구체적인 게 위로 가야 함
     * ============================================
     */
    public static String extractTheme(String name) {
        if (name == null) return "기타";
        String n = name.toUpperCase().replace(" ", "");

        // 🚨 0. [위험/파생] 레버리지 / 인버스 (초보자 주의!)
        // -> 이걸 맨 위에 둬서 다른 테마보다 먼저 걸러내는 게 좋음
        if (n.contains("레버리지") || n.contains("인버스") || n.contains("2X") || n.contains("선물")) {
            return "파생/레버리지"; 
        }

        // 1. [성장] 2차전지 / 전기차
        if (n.contains("2차전지") || n.contains("배터리") || n.contains("BATTERY")
                || n.contains("전기차") || n.contains("EV") || n.contains("에코프로")) {
            return "2차전지/전기차";
        }

        // 2. [성장] 반도체
        if (n.contains("반도체") || n.contains("SEMICON") || n.contains("칩") 
                || n.contains("삼성전자") || n.contains("하이닉스") || n.contains("SOXX")) {
            return "반도체";
        }

        // 3. [성장] AI / 로봇 / 메타버스
        if (n.contains("AI") || n.contains("인공지능") || n.contains("로봇") 
                || n.contains("ROBOT") || n.contains("메타버스")) {
            return "AI/로봇";
        }
        
        // ✨ 3-1. [성장] 우주 / 방산 
        if (n.contains("방산") || n.contains("우주") || n.contains("AEROSPACE") 
             || n.contains("DEFENSE")) {
            return "우주/방산";
        }

        // 4. [성장] 바이오 / 헬스케어
        if (n.contains("바이오") || n.contains("BIO") || n.contains("헬스") 
                || n.contains("HEALTH") || n.contains("제약") || n.contains("의료")) {
            return "바이오/헬스";
        }
        
        // ✨ 4-1. [성장/가치] 에너지 / 친환경 / 원자력
        if (n.contains("원자력") || n.contains("ATOM") || n.contains("태양광") 
             || n.contains("수소") || n.contains("신재생") || n.contains("에너지")
             || n.contains("기후") || n.contains("탄소") || n.contains("클린")) {
            return "에너지/환경";
        }
        
        // ✨ 4-2. [소비] 화장품 / 여행 / 게임 / 컨텐츠
        if (n.contains("화장품") || n.contains("여행") || n.contains("레저")
             || n.contains("게임") || n.contains("GAME") || n.contains("웹툰")
             || n.contains("미디어") || n.contains("컨텐츠") || n.contains("엔터")
             || n.contains("K-POP") || n.contains("푸드") || n.contains("소비")) {
            return "소비재/컨텐츠";
        }

        // 5. [자산배분] TDF / EMP
        if (n.contains("TDF") || n.contains("자산배분") || n.contains("TRF") || n.contains("EMP")) {
            return "자산배분/TDF";
        }
        
        // ✨ 5-1. [안전/현금] 채권 / 금리 
        if (n.contains("채권") || n.contains("BOND") || n.contains("국채") 
                || n.contains("국고채") || n.contains("TREASURY") || n.contains("TIP") 
                || n.contains("CD") || n.contains("KOFR") || n.contains("SOFR") 
                || n.contains("머니마켓") || n.contains("달러") || n.contains("단기")) { 
            return "채권/금리";
        }

        // 6. [배당] 리츠 / 인프라 / 부동산
        if (n.contains("리츠") || n.contains("REITS") || n.contains("인프라") 
                || n.contains("INFRA") || n.contains("부동산")) {
            return "리츠/부동산";
        }

        // 7. [배당] 배당주
        if (n.contains("배당") || n.contains("DIV") || n.contains("YIELD") 
                || n.contains("ARISTOCRATS") || n.contains("우선주") || n.contains("주주가치")) {
            return "배당";
        }

        // 8. [안전] 금융 / 은행
        if (n.contains("금융") || n.contains("은행") || n.contains("FINANCE") 
                || n.contains("BANK") || n.contains("증권") || n.contains("보험")) {
            return "금융";
        }

        // 9. [원자재] 금 / 은 / 원유
        if (n.contains("골드") || n.contains("GOLD") || n.contains("실버") 
                || n.contains("SILVER") || n.contains("원유") || n.contains("OIL") 
                || n.contains("구리") || n.contains("원자재") || n.contains("콩") 
                || n.contains("농산물") || n.contains("철강")) {
            return "원자재";
        }

        // 10. [성장] IT / 테크
        if (n.contains("IT") || n.contains("TECH") || n.contains("기술") 
                || n.contains("소프트웨어") || n.contains("플랫폼") || n.contains("인터넷")
                || n.contains("나스닥100") || n.contains("FANG")) { 
            return "IT/테크";
        }

        // 11. [시장] 시장 대표 지수 (국가명 대폭 추가)
        if (n.contains("200") || n.contains("KOSPI") || n.contains("KOSDAQ") || n.contains("코스닥")
                || n.contains("지수") || n.contains("INDEX") || n.contains("시장")
                || n.contains("S&P") || n.contains("SP500") || n.contains("다우")
                || n.contains("MSCI") || n.contains("TOP") 
                || n.contains("인도") || n.contains("INDIA") || n.contains("베트남")
                || n.contains("차이나") || n.contains("중국") || n.contains("일본") || n.contains("JAPAN")
                || n.contains("미국") || n.contains("유로") || n.contains("글로벌")
                || n.contains("그룹") || n.contains("밸류업")) { // 삼성그룹주, 밸류업 등도 시장으로
            return "시장대표";
        }

        return "기타";
    }

    // 3. 위험등급 분류
    public static int convertRiskToInt(String volatilityText) {
        if (volatilityText == null) return 3;
        String v = volatilityText.replace(" ", "").trim();
        if (v.contains("매우높") || v.contains("초고")) return 1;
        if (v.contains("높음") || v.equals("높")) return 2;
        if (v.contains("보통") || v.contains("중간")) return 3;
        if (v.contains("낮음") || v.equals("낮")) return 4;
        if (v.contains("매우낮") || v.contains("초저")) return 5;
        return 3;
    }
}