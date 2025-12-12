import http from "@/util/http-common";

// ==============================
// Ranking API
// ==============================

// 실시간 조회수 랭킹 (1시간)
export const getHourlyViewRanking = () => {
  return http.get("/ranking/etf/view/hourly");
};

// 일간 조회수 랭킹
export const getDailyViewRanking = () => {
  return http.get("/ranking/etf/view/daily");
};

// 월간 조회수 랭킹
export const getMonthlyViewRanking = () => {
  return http.get("/ranking/etf/view/monthly");
};

// 실시간 검색어 랭킹 (1시간)
export const getHourlySearchRanking = () => {
  return http.get("/ranking/search/hourly");
};

// 일간 검색어 랭킹
export const getDailySearchRanking = () => {
  return http.get("/ranking/search/daily");
};

// 월간 검색어 랭킹
export const getMonthlySearchRanking = () => {
  return http.get("/ranking/search/monthly");
};
