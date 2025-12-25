package com.etfinder.mvc.comment.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.comment.dto.Comment;
import com.etfinder.mvc.comment.dto.CommentResponse;

@Mapper
public interface CommentMapper {
	
	// 1. 한줄평 등록
	int addComment(Comment comment);

	// 2. 한줄평 조회 (etfId로 조회)
	List<CommentResponse> getCommentsByEtfId(@Param("etfId") Long etfId);

	// 3. 한줄평 수정 (작성자만 가능)
	int updateComment(@Param("commentId") Long commentId,
					@Param("userId") Long userId,
					@Param("content") String content);

	// 4. 한줄평 삭제 (작성자만 가능)
	int deleteComment(@Param("commentId") Long commentId,
					@Param("userId") Long userId);
	
	// 5. 한줄평 ai 분석 결과 저장
	int updateCommentSentiment(@Param("commentId") Long commentId, @Param("sentiment") String sentiment);
	
	// 6. 한줄평 ai 분석 통계 가져오기
	Map<String, Object> selectSentimentStat(@Param("etfId") Long etfId);
}
