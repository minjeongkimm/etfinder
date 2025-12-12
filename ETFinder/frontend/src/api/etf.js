import http from "@/util/http-common";

// ==============================
// ETF 조회 API
// ==============================

// 1. 전체 ETF 목록 조회
export const getEtfs = () => {
  return http.get("/etfs");
};

// 2. 고급 검색 (SearchCondition 파라미터 지원)
// 백엔드: @GetMapping("/search") public ResponseEntity<?> search(...)
// SearchCondition 필드: keyword, market, theme, minFee, maxFee, minAum, maxAum, riskRating, orderBy, orderDir
export const searchEtfs = (params) => {
  return http.get("/etfs/search", { params });
};

// 3. ETF 상세 조회
export const getEtfDetail = (etfId) => {
  return http.get(`/etfs/${etfId}`);
};

// ==============================
// 관리자 전용 API (EtfController)
// ==============================

// 4. ETF 등록
export const insertEtf = (etfData) => {
  return http.post("/etfs", etfData);
};

// 5. ETF 수정
export const updateEtf = (etfCode, etfData) => {
  return http.patch(`/etfs/${etfCode}`, etfData);
};

// 6. ETF 삭제
export const deleteEtf = (etfCode) => {
  return http.delete(`/etfs/${etfCode}`);
};