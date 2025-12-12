import http from "@/util/http-common";

// ==============================
// Bookmark (Portfolio) API
// ==============================

/**
 * 북마크 추가
 * POST /api/bookmarks/{etfId}
 */
export const addBookmark = (etfId) => {
  return http.post(`/bookmarks/${etfId}`);
};

/**
 * 북마크 해제
 * DELETE /api/bookmarks/{etfId}
 */
export const removeBookmark = (etfId) => {
  return http.delete(`/bookmarks/${etfId}`);
};

/**
 * 내 북마크 목록 조회
 * GET /api/bookmarks
 * @returns {Promise} List<Bookmark>
 */
export const getMyBookmarks = () => {
  return http.get("/bookmarks");
};
