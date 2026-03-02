import http from "@/util/http-common";

// ==============================
// Like (Wishlist) API
// ==============================

/**
 * 좋아요 추가
 * POST /api/likes/{etfId}
 */
export const addLike = (etfId) => {
  return http.post(`/likes/${etfId}`);
};

/**
 * 좋아요 해제
 * DELETE /api/likes/{etfId}
 */
export const removeLike = (etfId) => {
  return http.delete(`/likes/${etfId}`);
};

/**
 * 내가 좋아요한 ETF 목록 조회
 * GET /api/users/me/likes
 * @returns {Promise} List<EtfProduct>
 */
export const getMyLikes = () => {
  return http.get("/users/me/likes");
};
