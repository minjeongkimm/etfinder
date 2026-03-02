package com.etfinder.mvc.comment.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.comment.dto.CommentAiResponse;
import com.etfinder.mvc.comment.mapper.CommentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentAiServiceImpl implements CommentAiService{

	private final CommentMapper commentMapper;
	private final ChatClient chatClient;
	private final ObjectMapper objectMapper;
	
	public CommentAiServiceImpl(CommentMapper commentMapper, ChatClient chatClient, ObjectMapper objectMapper) {
		this.commentMapper = commentMapper;
		this.chatClient = chatClient;
		this.objectMapper = objectMapper;
	}

	@Async
	@Override
	public void analyzeAndUpdate(Long commentId, String content) {
		
		log.info("비동기 AI 분석 시작: commentId={}", commentId);
		
		try {
            // 1. 프롬프트 준비
			String systemMsg = """
				    당신은 한국의 주식 커뮤니티(종토방) 댓글 분석 전문가입니다.
				    사용자의 댓글을 분석하여 **[POSITIVE, NEGATIVE, NEUTRAL]** 중 하나로 분류하세요.
				    
				    [핵심 규칙]
				    1. 문장의 '표면적 의미'보다 **'작성자의 속마음(감정)'**을 우선하세요.
				    2. 욕설이 섞여 있어도 주가가 올라서 기뻐하는 것이라면 POSITIVE입니다. (예: "와 미친 떡상 ㅋㅋㅋ")
				    3. 반어법을 주의하세요. (예: "참 잘~ 떨어지네" -> NEGATIVE)
				    
				    [판단 기준]
				    1. **POSITIVE (긍정/매수우위)**: 주가 상승 기대, 매수 추천, 수익 인증, 좋은 뉴스 언급, '가즈아', '떡상', '호재' 등.
				    2. **NEGATIVE (부정/매도우위)**: 주가 하락 우려, 매도 추천, 손실 하소연, 악재 언급, '떡락', '나락', '돔황챠(도망쳐)', '흑우' 등.
				    3. **NEUTRAL (중립)**: 단순 정보 공유, 질문, 감정이 드러나지 않는 글.
				    
				    [주의사항]
				    - 인버스(Inverse) 상품인 경우: "떨어진다"는 말이 수익을 의미하므로 문맥을 잘 파악하세요. (하지만 복잡하므로 일단은 문장의 뉘앙스 자체에 집중하세요)
				    - JSON 형식으로 `{"sentiment": "POSITIVE"}` 형태로만 응답하세요.
				    """;
            
            // 2. AI 호출
			String jsonResponse = chatClient.prompt()
                    .system(systemMsg)  // 시스템 역할 부여
                    .user(content)      // 사용자 댓글 입력
                    .call()
                    .content();         // 결과 문자열 받기
			
			// 3. JSON 파싱 (문자열 -> 객체 -> 알맹이 꺼내기)
			CommentAiResponse result = objectMapper.readValue(jsonResponse, CommentAiResponse.class);
            String sentimentValue = result.getSentiment();

            // 5. DB 업데이트
            commentMapper.updateCommentSentiment(commentId, sentimentValue);
            log.info("감성 분석 완료: {} -> {}", commentId, sentimentValue);

        } catch (Exception e) {
            log.error("AI 분석 실패", e);
        }
	}
}
