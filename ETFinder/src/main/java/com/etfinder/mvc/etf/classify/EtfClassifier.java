package com.etfinder.mvc.etf.classify;

public class EtfClassifier {

    // 1. 시장(Market) 분류 (기존 유지 + 조금 보완)
    public static String extractMarket(String name) {
        if (name == null) return "KOR";
        String n = name.toUpperCase().replace(" ", "");

        if (n.contains("NASDAQ") || n.contains("S&P") || n.contains("SP500")
                || n.contains("다우") || n.contains("미국") || n.contains("US") 
                || n.contains("CHINA") || n.contains("중국") || n.contains("HONGKONG")
                || n.contains("GLOBAL") || n.contains("글로벌")) {
            return "USA"; // 편의상 해외는 USA로 통칭하거나 'GLOBAL'로 분리해도 됨
        }
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
        String n = name.toUpperCase().replace(" ", ""); // 공백 제거하고 비교

        // 1️⃣ [성장] 2차전지 / 전기차
        if (n.contains("2차전지") || n.contains("배터리") || n.contains("BATTERY")
                || n.contains("전기차") || n.contains("EV") || n.contains("에코프로")) {
            return "2차전지/전기차";
        }

        // 2️⃣ [성장] 반도체
        if (n.contains("반도체") || n.contains("SEMICON") || n.contains("칩") 
                || n.contains("삼성전자") || n.contains("하이닉스") || n.contains("SOXX")) {
            return "반도체";
        }

        // 3️⃣ [성장] AI / 로봇 / 메타버스
        if (n.contains("AI") || n.contains("인공지능") || n.contains("로봇") 
                || n.contains("ROBOT") || n.contains("메타버스") || n.contains("METAVERSE")) {
            return "AI/로봇";
        }

        // 4️⃣ [성장] 바이오 / 헬스케어 (New!)
        if (n.contains("바이오") || n.contains("BIO") || n.contains("헬스") 
                || n.contains("HEALTH") || n.contains("제약")) {
            return "바이오";
        }

        // 5️⃣ [안전/현금] 채권 / 금리 (키워드 대폭 추가!)
        if (n.contains("채권") || n.contains("BOND") || n.contains("국채") 
                || n.contains("국고채") || n.contains("TREASURY") || n.contains("TIP") // 물가연동채
                || n.contains("CD") || n.contains("KOFR") || n.contains("SOFR") // 파킹통장형 금리 ETF
                || n.contains("머니마켓") || n.contains("달러") || n.contains("단기")) {
            return "채권/금리";
        }

        // 6️⃣ [배당] 리츠 / 인프라 / 부동산 (New!)
        // -> 아까 결과에 나온 '리츠인프라'를 구제하기 위함
        if (n.contains("리츠") || n.contains("REITS") || n.contains("인프라") 
                || n.contains("INFRA") || n.contains("부동산")) {
            return "리츠/부동산";
        }

        // 7️⃣ [배당] 배당주
        if (n.contains("배당") || n.contains("DIV") || n.contains("YIELD") 
                || n.contains("ARISTOCRATS") || n.contains("우선주")) {
            return "배당";
        }

        // 8️⃣ [안전] 금융 / 은행
        if (n.contains("금융") || n.contains("은행") || n.contains("FINANCE") 
                || n.contains("BANK") || n.contains("증권") || n.contains("보험")) {
            return "금융";
        }

        // 9️⃣ [원자재] 금 / 은 / 원유 (New!)
        if (n.contains("골드") || n.contains("GOLD") || n.contains("실버") 
                || n.contains("SILVER") || n.contains("원유") || n.contains("OIL") 
                || n.contains("구리") || n.contains("원자재") || n.contains("콩")) {
            return "원자재";
        }

        // 🔟 [성장] IT / 테크 (범위가 넓으니 뒤쪽 배치)
        if (n.contains("IT") || n.contains("TECH") || n.contains("기술") 
                || n.contains("소프트웨어") || n.contains("플랫폼") || n.contains("인터넷")
                || n.contains("나스닥100") || n.contains("FANG")) { // 나스닥100은 기술주 성향이 강함
            return "IT/테크";
        }

        // 1️⃣1️⃣ [시장] 시장 대표 지수
        if (n.contains("200") || n.contains("KOSPI") || n.contains("KOSDAQ") || n.contains("코스닥")
                || n.contains("지수") || n.contains("INDEX") || n.contains("시장")
                || n.contains("S&P") || n.contains("SP500") || n.contains("다우")
                || n.contains("MSCI") || n.contains("TOP")) {
            return "시장대표";
        }

        return "기타";
    }

    // 3. 위험등급 분류 (기존 유지)
    public static int convertRiskToInt(String volatilityText) {
        // ... 기존 코드 그대로 사용 ...
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