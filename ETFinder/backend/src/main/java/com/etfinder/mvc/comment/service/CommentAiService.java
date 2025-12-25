package com.etfinder.mvc.comment.service;

public interface CommentAiService {

	void analyzeAndUpdate(Long commentId, String content);
}
