import http from "@/util/http-common";

/**
 * 한줄평(댓글) API 모듈
 * 백엔드 경로: /api/etfs/{etfId}/comments
 */

/**
 * 특정 ETF의 한줄평 목록 조회
 * @param {number} etfId - ETF ID
 * @returns {Promise<CommentListResponse>} 댓글 목록 및 AI 분석 정보
 * CommentListResponse: {
 *   comments: CommentResponse[],
 *   positivePercent: number,
 *   negativePercent: number,
 *   totalCnt: number,
 *   moodMessage: string
 * }
 */
export const getComments = async (etfId) => {
  try {
    const response = await http.get(`/etfs/${etfId}/comments`);
    // 백엔드가 CommentListResponse 전체 구조를 반환
    return response.data || { comments: [], positivePercent: 0, negativePercent: 0, totalCnt: 0, moodMessage: '' };
  } catch (error) {
    // 204 No Content 처리
    if (error.response?.status === 204) {
      return { comments: [], positivePercent: 0, negativePercent: 0, totalCnt: 0, moodMessage: '' };
    }
    throw error;
  }
};

/**
 * 한줄평 등록
 * @param {number} etfId - ETF ID
 * @param {string} content - 댓글 내용
 * @returns {Promise<void>}
 */
export const addComment = async (etfId, content) => {
  // ⚠️ 중요: userId, etfId를 body에 포함하지 않음 (서버가 자동 처리)
  return http.post(`/etfs/${etfId}/comments`, {
    content: content.trim()
  });
};

/**
 * 한줄평 수정
 * @param {number} etfId - ETF ID
 * @param {number} commentId - 댓글 ID
 * @param {string} content - 수정할 내용
 * @returns {Promise<void>}
 */
export const updateComment = async (etfId, commentId, content) => {
  return http.put(`/etfs/${etfId}/comments/${commentId}`, {
    content: content.trim()
  });
};

/**
 * 한줄평 삭제
 * @param {number} etfId - ETF ID
 * @param {number} commentId - 댓글 ID
 * @returns {Promise<void>}
 */
export const deleteComment = async (etfId, commentId) => {
  return http.delete(`/etfs/${etfId}/comments/${commentId}`);
};
