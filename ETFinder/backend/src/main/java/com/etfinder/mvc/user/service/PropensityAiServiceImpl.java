package com.etfinder.mvc.user.service;

import org.springframework.ai.chat.client.ChatClient;

import com.etfinder.mvc.user.dto.PropensityAiResponse;

public class PropensityAiServiceImpl implements PropensityAiService{
	
	private final ChatClient chatClient;
	
	public PropensityAiServiceImpl(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public PropensityAiResponse analyzePropensity(String type, String summary) {
		// 1. 시스템 프롬프트 (AI의 역할 설정)
        String systemMsg = "당신은 2030 초보 투자자를 위한 친절한 금융 멘토입니다. 반말(~해요)을 사용하세요.";

        // 2. 유저 프롬프트
        String userMsg = String.format("""
                분석 요청:
                - 확정 성향: %s
                - 답변 요약: %s
                
                위 내용을 바탕으로 JSON 포맷(title, reason, advice, cheering)에 맞춰 분석해주세요.
                """, type, summary);

        return chatClient.prompt()
                .system(systemMsg)
                .user(userMsg)
                .call()
                .entity(PropensityAiResponse.class);
	}

}
