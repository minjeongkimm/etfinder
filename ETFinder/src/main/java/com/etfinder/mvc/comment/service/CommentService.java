package com.etfinder.mvc.comment.service;

import java.util.List;

import com.etfinder.mvc.comment.dto.Comment;

public interface CommentService {
	
	// 1. 한줄평 등록
	int addComment(Comment comment);
	
	// 2. 한줄평 조회
	List<Comment> commentsList(Long etfId);
	
	// 3. 한줄평 수정
	int updateComment(Long commentId, String content);
	
	// 4. 한줄평 삭제 
	int deleteComment(Long commentId);
	
	
}
