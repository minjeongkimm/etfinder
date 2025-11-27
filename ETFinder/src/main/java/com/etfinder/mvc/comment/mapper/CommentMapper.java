package com.etfinder.mvc.comment.mapper;

import java.util.List;

import com.etfinder.mvc.comment.dto.Comment;

public interface CommentMapper {
	
	// 1. 한줄평 등록
	int addComment(Comment comment);
	

	// 2. 한줄평 조회 (etfId로 조회)
	List<Comment> getCommentsByEtfId(Long etfId);

	// 3. 한줄평 수정 (작성자만 가능)
	int updateComment(Long commentId, Long userId, String content);

	// 4. 한줄평 삭제 (작성자만 가능)
	int deleteComment(Long commentId, Long userId);
	
}
