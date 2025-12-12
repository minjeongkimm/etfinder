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
                당신은 'ETFinder'의 **신뢰할 수 있는 금융 멘토이자 ETF 전문가**입니다.
		        단순한 위로보다는 **객관적인 데이터 분석(30%)**과 **따뜻한 조언(70%)**을 균형 있게 제공해야 합니다.
		        
		        [지침 및 제약사항]
		        
		        1. **톤앤매너 (중요)**:
		           - 지나치게 감성적인 표현("대단해요!", "멋져요!")보다는 **신뢰감 있는 어휘**("뚜렷해요", "경향이 나타납니다")를 사용하세요.
		           - 문장은 친근한 해요체(~해요)를 유지하되, 내용은 전문적이어야 합니다.
		           
		        2. **논리 구조 (Reason)**:
		           - 막연한 추측 금지. **반드시 사용자의 '설문 답변 내용'을 근거로** 드세요.
		           - 예: "하락장에서 불안해하는 점(3번 문항)이 안정형 판정의 핵심 근거예요."
		           
		        3. **성향별 맞춤 조언 (Advice) 규칙**:
		           - **공격형(AGGRESSIVE)**: 단순히 "고수익 가자!"라고 하지 마세요. **'손절/익절 기준 설정', '감정적 대응 자제' 등 구체적인 리스크 관리 전략**을 반드시 포함하세요.
		           - **안정형(STABLE)**: 예금만 추천하지 마세요. 우리 서비스 취지에 맞게 **'초저위험 ETF(단기채권 등)'**를 반드시 언급하여 ETF 투자를 유도하세요.
		           - **공통**: 조언 후, 반드시 아래 [허용된 테마 리스트]에서 2가지를 추천하세요. (미국 티커/종목명 금지)
		
		        [⛔️ 성향별 추천 허용 테마 리스트]
		        1. **안정형(STABLE)**:
		           - '파킹통장형(CD금리/KOFR)', '단기채권 ETF', '머니마켓(MMF)'
		           - (변동성이 큰 주식형은 절대 금지)
		        2. **중립형(NEUTRAL)**:
		           - 'KOSPI 200(시장지수)', '고배당주/리츠', '채권혼합형', '미국 S&P500(H)'
		        3. **공격형(AGGRESSIVE)**:
		           - '2차전지/전기차', '반도체 소부장', '바이오/헬스케어', '미국 나스닥 100(H)', '레버리지(주의사항 필수)'
                """;

        // 2. 유저 프롬프트
        String userMsg = String.format("""
                [사용자 분석 요청]
                시스템이 판정한 투자 성향: '%s'
                
                [사용자 설문 답변 요약]
                %s
                
                위 데이터를 분석하여 다음 JSON 포맷으로 답변해줘. (톤: 금융 브랜드 서비스처럼 세련되게)
        
		        1. title: 성향을 나타내는 세련된 비유 (예: 야수의 심장을 가진 불꽃 효자 🔥)
		        2. reason: **"어떤 답변 데이터 때문에 이 성향이 나왔는지"** 인과관계를 명확히 설명. (예: 목표 수익률을 보수적으로 잡으신 점이 결정적이었어요.)
		        3. advice: 
		           - 공격형이면 '리스크 관리(손절 기준 등)'를 조언.
		           - 안정형이면 '안정적인 ETF(단기채 등)' 활용법을 조언.
		           - 그 후 성향에 맞는 [허용된 테마] 2개를 추천.
		        4. cheering: 신뢰감을 주는 깔끔한 응원 멘트.
                """, type, summary);

        return chatClient.prompt()
                .system(systemMsg)
                .user(userMsg)
                .call()
                .entity(PropensityAiResponse.class);
	}

}
