import http from "@/util/http-common";

// 1. ETF 검색
// 백엔드: @GetMapping("/search") public ResponseEntity<?> search(...)
export const getEtfList = (keyword) => {
  // 검색어가 있으면 /search, 없으면 목록 조회로 분기
  
  if (keyword) {
    return http.get("/etfs/search", {
      params: {
        keyword: keyword, // SearchCondition 객체의 필드명과 일치해야 함
      },
    });
  } else {
    return http.get("/etfs"); // 전체 조회
  }
};

// 2. 상세 조회
export const getEtfDetail = (etfId) => {
  return http.get(`/etfs/${etfId}`);
};

// ==============================
// 관리자 전용 API (EtfController)
// ==============================

// 3. ETF 등록
export const insertEtf = (etfData) => {
  return http.post("/etfs", etfData);
};

// 4. ETF 수정
export const updateEtf = (etfCode, etfData) => {
  return http.patch(`/etfs/${etfCode}`, etfData);
};

// 5. ETF 삭제
export const deleteEtf = (etfCode) => {
  return http.delete(`/etfs/${etfCode}`);
};