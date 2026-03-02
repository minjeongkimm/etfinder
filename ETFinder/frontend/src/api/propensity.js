import http from "@/util/http-common";

// ==============================
// Propensity Test API
// ==============================

/**
 * 투자 성향 테스트 제출
 * @param {number[]} answers - 각 질문의 선택된 점수 배열 (10/20/30/40)
 * @returns {Promise} PropensityResult 객체
 */
export const submitPropensityTest = (answers) => {
  return http.post("/propensity/test", { answers });
};


