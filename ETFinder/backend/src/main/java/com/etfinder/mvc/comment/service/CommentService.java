package com.etfinder.mvc.comment.service;

import com.etfinder.mvc.comment.dto.Comment;
import com.etfinder.mvc.comment.dto.CommentListResponse;

public interface CommentService {
	
	/*
	 * 1. 한줄평 등록
	 * - comment 안에 userId, etfId, content 셋팅해서 넘김
	 * - 성공 시 1, 실패 시 0
	 */
	int addComment(Comment comment);
	

	/*
	 * 2. 한줄평 조회 (특정 ETF의 전체 댓글)
	 */
	CommentListResponse getCommentsByEtfId(Long etfId);

	/*
	 * 3. 한줄평 수정
	 * - commentId + userId로 권한 체크 후 수정
	 * - content만 수정, updated_at은 DB에서 자동 갱신
	 * - 성공 시 1, 실패 시 0
	 */
	int updateComment(Long commentId, Long userId, String content);

	/*
	 * 4. 한줄평 삭제
	 * - commentId + userId로 권한 체크 후 삭제
	 * - 성공 시 1, 실패 시 0
	 */
	int deleteComment(Long commentId, Long userId);
	
	
}
