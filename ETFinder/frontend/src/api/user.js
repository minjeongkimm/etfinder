import http from "@/util/http-common";

// ==============================
// User API
// ==============================

// 내 정보 조회
export const getMyInfo = () => {
  return http.get("/users/me");
};

// 내 정보 수정
export const updateMyInfo = (userData) => {
  return http.patch("/users/me", userData);
};

// 회원 탈퇴
export const deleteUser = () => {
  return http.delete("/users/me");
};

// ==============================
// 추천 ETF API
// ==============================

// 사용자 맞춤 추천 ETF 조회 (My Page용)
export const getMyRecommendedEtfs = () => {
  return http.get("/etfs/recommend");
};
