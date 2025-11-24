package com.etfinder.util;

public class EtfClassifier {

    /**
     * ============================================
     *  1. 시장(Market) 분류
     *     - ETF 이름만 보고 판별
     * ============================================
     */
    public static String extractMarket(String name) {
        if (name == null) return "KOR";

        String n = name.toUpperCase().replace(" ", "");

        // 🇺🇸 미국 시장
        if (n.contains("NASDAQ") || n.contains("S&P") || n.contains("SP500")
                || n.contains("다우") || n.contains("미국")
                || n.contains("US") || n.contains("USA")) {
            return "USA";
        }

        // 🇰🇷 한국 시장 (KODEX, TIGER 등)
        if (n.contains("KODEX") || n.contains("TIGER") || n.contains("KINDEX")
                || n.contains("ARIRANG") || n.contains("KOSEF")) {
            return "KOR";
        }

        // 기본값
        return "KOR";
    }


    /**
     * ============================================
     *  2. 테마(Theme) 분류
     *     - ETF 이름만 분석
     *     - 2차전지/배터리/전기차 최우선!
     * ============================================
     */
    public static String extractTheme(String name) {
        if (name == null) return "기타";

        String n = name.toUpperCase();

        // ===========================
        // 2차전지 / 전기차 / 배터리
        // ===========================
        if (n.contains("2차전지") || n.contains("배터리") || n.contains("BATTERY")
                || n.contains("전기차") || n.contains("EV")) {
            return "2차전지/전기차";
        }

        // ===========================
        // 반도체
        // ===========================
        if (n.contains("반도체") || n.contains("SEMICON")) {
            return "반도체";
        }

        // ===========================
        // AI / 로봇
        // ===========================
        if (n.contains("AI") || n.contains("인공지능")
                || n.contains("로봇") || n.contains("ROBOT")) {
            return "AI";
        }

        // ===========================
        // 배당
        // ===========================
        if (n.contains("배당") || n.contains("DIV") || n.contains("YIELD")) {
            return "배당";
        }

        // ===========================
        // 금융
        // ===========================
        if (n.contains("금융") || n.contains("은행")
                || n.contains("FINANCE") || n.contains("BANK")) {
            return "금융";
        }

        // ===========================
        // IT / 테크
        // ===========================
        if (n.contains("IT") || n.contains("TECH")
                || n.contains("기술") || n.contains("SOFTWARE")
                || n.contains("플랫폼")) {
            return "IT/테크";
        }

        // ===========================
        // 채권
        // ===========================
        if (n.contains("채권") || n.contains("BOND")) {
            return "채권";
        }

        // ===========================
        // 시장 대표 지수형
        // ===========================
        if (n.contains("200") || n.contains("KOSPI") || n.contains("KOSDAQ")
                || n.contains("지수") || n.contains("INDEX")) {
            return "시장대표";
        }

        // ===========================
        // Default
        // ===========================
        return "기타";
    }


    /**
     * ============================================
     *  3. 위험등급(Risk Rating) 1~5 등급 변환
     *     - 변동성 텍스트 기반
     * ============================================
     */
    public static int convertRiskToInt(String volatilityText) {
        if (volatilityText == null) return 3; // 기본: 보통

        String v = volatilityText.replace(" ", "").trim();

        if (v.contains("매우높") || v.contains("초고"))
            return 1;  // 가장 위험

        if (v.contains("높음") || v.equals("높"))
            return 2;

        if (v.contains("보통") || v.contains("중간"))
            return 3;

        if (v.contains("낮음") || v.equals("낮"))
            return 4;

        if (v.contains("매우낮") || v.contains("초저"))
            return 5;  // 가장 안전

        return 3; // 기본값
    }
}
