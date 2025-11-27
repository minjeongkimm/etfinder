package com.etfinder.mvc.comment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.comment.dto.Comment;
import com.etfinder.mvc.comment.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public int addComment(Comment comment) {
		return commentMapper.addComment(comment);
	}

	@Override
	public List<Comment> getCommentsByEtfId(Long etfId) {
		return commentMapper.getCommentsByEtfId(etfId);
	}

	@Override
	public int updateComment(Long commentId, Long userId, String content) {
		return commentMapper.updateComment(commentId, userId, content);
	}

	@Override
	public int deleteComment(Long commentId, Long userId) {
		return commentMapper.deleteComment(commentId, userId);
	}

}
