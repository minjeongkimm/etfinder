package com.etfinder.mvc.comment.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.comment.dto.Comment;
import com.etfinder.mvc.comment.dto.CommentListResponse;
import com.etfinder.mvc.comment.dto.CommentResponse;
import com.etfinder.mvc.comment.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {

	private final CommentMapper commentMapper;
	private final CommentAiService commentAiService;
	
	public CommentServiceImpl(CommentMapper commentMapper, CommentAiService commentAiService) {
		this.commentMapper = commentMapper;
		this.commentAiService = commentAiService;
	}

	@Override
	public int addComment(Comment comment) {
		// 1. 한줄평 저장
		int result = commentMapper.addComment(comment);
		
		// 2. 저장완료 시 AI 분석 요청 (비동기라서 기다리지 않고 넘어감)
		if (result == 1) {
            commentAiService.analyzeAndUpdate(comment.getCommentId(), comment.getContent());
        }
		
		return result;
	}

	@Override
    public CommentListResponse getCommentsByEtfId(Long etfId) {
		
		CommentListResponse response = new CommentListResponse();

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
        
        response.setComments(list);
        
        Map<String, Object> stat = commentMapper.selectSentimentStat(etfId);
        long total = (stat != null && stat.get("totalCount") != null) ? ((Number) stat.get("totalCount")).longValue() : 0L;
        long pos = (stat != null && stat.get("positiveCount") != null) ? ((Number) stat.get("positiveCount")).longValue() : 0L;
        long neg = (stat != null && stat.get("negativeCount") != null) ? ((Number) stat.get("negativeCount")).longValue() : 0L;

        response.setTotalCnt(total);
        
        if (total >= 5) { // 표본이 5개 이상일 때만 분석
            
            int posPer = (int) ((pos * 100.0) / total);
            int negPer = (int) ((neg * 100.0) / total);
            int neutralPer = 100 - (posPer + negPer); // 나머지는 다 중립

            response.setPositivePercent(posPer);
            response.setNegativePercent(negPer);

            String message = "";

            // 우선순위 1: 중립(관망)이 과반수일 때
            if (neutralPer >= 50) {
                message = "투자자들이 신중하게 관망하고 있습니다. (중립 우세) 🤔";
            }
            // 우선순위 2: 긍정이 압도적일 때
            else if (posPer >= 70) {
                message = "투자 심리가 매우 강한 매수세입니다. 📈";
            }
            // 우선순위 3: 긍정이 우세할 때
            else if (posPer >= 55) {
                message = "긍정적인 전망이 다소 우세합니다.";
            }
            // 우선순위 4: 부정이 압도적일 때
            else if (negPer >= 70) {
                message = "시장 분위기가 냉각되었습니다. 주의가 필요합니다. ❄️";
            }
            // 우선순위 5: 부정이 우세할 때
            else if (negPer >= 55) {
                message = "하락을 우려하는 목소리가 높습니다.";
            }
            // 그 외: 팽팽할 때
            else {
                message = "매수와 매도 의견이 팽팽하게 맞서고 있습니다. ⚖️";
            }
            
            response.setMoodMessage(message);

        } else {
            // 데이터 부족 시 (Cold Start)
            response.setPositivePercent(0);
            response.setNegativePercent(0);
            response.setMoodMessage("아직 데이터가 부족합니다. 의견을 남겨주세요! 💬");
        }

        return response;
    }

	@Override
	public int updateComment(Long commentId, Long userId, String content) {
		// 업데이트 시에도 바뀐 내용을 ai 분석하도록 로직 추가
		int result = commentMapper.updateComment(commentId, userId, content);
	    if (result == 1) {
	        commentAiService.analyzeAndUpdate(commentId, content);
	    }
		return result;
	}

	@Override
	public int deleteComment(Long commentId, Long userId) {
		return commentMapper.deleteComment(commentId, userId);
	}

}
