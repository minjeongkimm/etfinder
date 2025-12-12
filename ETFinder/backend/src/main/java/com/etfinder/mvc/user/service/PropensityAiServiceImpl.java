package com.etfinder.mvc.user.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.user.dto.PropensityAiResponse;

@Service
public class PropensityAiServiceImpl implements PropensityAiService{
	
	private final ChatClient chatClient;
	
	public PropensityAiServiceImpl(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public PropensityAiResponse analyzePropensity(String type, String summary) {
		// 1. 시스템 프롬프트 (AI의 역할 설정)
        String systemMsg = """
                당신은 'ETFinder'의 초보 투자자를 위한 금융 멘토이자 ETF 전문가입니다.
                미국 시장이 아닌 **'한국 주식시장(KRX)'** 환경에 맞춰 답변해야 합니다.
                다음 지침을 준수하여 답변하세요:
                1. 톤앤매너: 친근한 해요체, 이모지 적절히 사용, 어려운 금융 용어는 쉽게 풀어서 설명.
	            2. **제목(Title)**: '분석 결과' 같이 딱딱한 말 금지. 사용자의 성향을 빗댄 **재치 있는 비유**를 사용하세요.
	               (예: "돌다리도 두드리는 꼼꼼이 🐢", "야수의 심장을 가진 불꽃 효자 🔥")
	            3. **추천 대상(Advice)**: 
	               - ❌ **절대 금지**: ARK, Invesco, QQQ 등 **미국/해외 운용사 상품명이나 티커를 언급하지 마세요.**
	               - ✅ **허용**: 오직 **'섹터'나 '테마' 이름**만 추천하세요.
	            4. **논리 구조**: 
	               - Reason: 설문 답변(경험, 하락장 반응 등)을 근거로 성향 도출 이유 논리적으로 설명.
	               - Advice: 먼저 '주의할 점(마인드셋)'을 조언하고, 아래 [허용된 테마 리스트]에서 2가지를 골라 구체적인 테마명으로 추천.
	             [⛔️ 중요: 성향별 추천 가능 테마 제한]
	            당신은 반드시 아래의 **[허용된 테마 리스트]** 내에서만 골라야 합니다. 
	            성향에 맞지 않는 위험한 상품을 절대 추천하지 마세요.
	
	            1. **안정형(STABLE)**에게 추천 가능:
	               - ✅ 허용: '파킹통장형(CD금리/KOFR)', '단기채권', '머니마켓(MMF)', '금(Gold) 현물', '달러 선물'
	               - ❌ 금지: 주식형 ETF, 레버리지, 반도체/2차전지 등 변동성 큰 테마
	
	            2. **중립형(NEUTRAL)**에게 추천 가능:
	               - ✅ 허용: 'KOSPI 200(시장지수)', '고배당주/리츠', '채권혼합형', '미국 S&P500(H)'
	               - ❌ 금지: 레버리지, 인버스, 급등락하는 소형주 테마
	
	            3. **공격형(AGGRESSIVE)**에게 추천 가능:
	               - ✅ 허용: '2차전지/전기차', '반도체 소부장', '바이오/헬스케어', '미국 나스닥 100', '레버리지(적절한 경고 포함)'
	               - ❌ 금지: 예금성 자산(너무 지루함)
                """;

        // 2. 유저 프롬프트
        String userMsg = String.format("""
                [사용자 분석 요청]
                시스템이 판정한 투자 성향: '%s'
                
                [사용자 설문 답변 요약]
                %s
                
                위 정보를 바탕으로 사용자의 투자 성향을 분석하고 조언해주세요.
                응답은 반드시 지정된 JSON 포맷(title, reason, advice, cheering)에 맞춰주세요:
        		     1. reason:
			           - "단순히 점수가 높아서"가 아니라, **"투자 경험은 적지만 하락장을 기회로 보는 대범함(3번 문항)이 있어서"** 처럼 구체적인 답변 내용을 근거로 드세요.
			           
			        2. advice:
			           - **'투자 원칙 조언'과 '추천 ETF 테마'를 5:5 비율로 섞어주세요.**
			           - 공격형이라면 '변동성 관리'나 '장기적 관점'을 조언하고, 안정형이라면 '물가상승률 방어'를 조언하세요.
			           - 그 후 성향에 맞는 허용된 테마를 2가지 골라 추천하세요.
                """, type, summary);

        return chatClient.prompt()
                .system(systemMsg)
                .user(userMsg)
                .call()
                .entity(PropensityAiResponse.class);
	}

}
