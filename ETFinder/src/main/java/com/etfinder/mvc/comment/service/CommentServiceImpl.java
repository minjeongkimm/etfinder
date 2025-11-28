package com.etfinder.mvc.comment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.comment.dto.Comment;
import com.etfinder.mvc.comment.dto.CommentResponse;
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
    public List<CommentResponse> getCommentsByEtfId(Long etfId) {

        List<CommentResponse> list = commentMapper.getCommentsByEtfId(etfId);

        for (CommentResponse res : list) {
            // createdAt / updatedAt 둘 다 DB에 값은 있지만,
            // "수정 여부"에 따라 하나만 노출되도록 정리
            if (res.getUpdatedAt() == null ||
                res.getUpdatedAt().isEqual(res.getCreatedAt())) {
                // 수정 안 함 → createdAt만 보이게
                res.setEdited(false);
                res.setUpdatedAt(null); // 프론트에 안 보이게
            } else {
                // 수정함 → updatedAt만 보이게
                res.setEdited(true);
                res.setCreatedAt(null); // 프론트에 안 보이게
            }
        }

        return list;
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
