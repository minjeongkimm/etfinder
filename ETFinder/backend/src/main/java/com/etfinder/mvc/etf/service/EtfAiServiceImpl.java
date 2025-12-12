package com.etfinder.mvc.etf.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.etf.dto.EtfAiDescriptionResponse;

@Service
public class EtfAiServiceImpl implements EtfAiService{
	
	private final ChatClient chatClient;

	public EtfAiServiceImpl(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public EtfAiDescriptionResponse generateDescription(String etfName, String etfCode, String category,
			int riskRating) {
		
		// 위험 등급을 AI가 이해하기 쉬운 멘트로 변환
		String riskContext = switch (riskRating) {
	        case 1 -> "매우 높은 위험 (초고위험)";
	        case 2 -> "높은 위험 (고위험)";
	        case 3 -> "다소 높은 위험 (중위험)";
	        case 4 -> "낮은 위험 (저위험)";
	        default -> "매우 낮은 위험 (초저위험)";
	    };
	    
	    String systemMsg = """
	            당신은 'ETFinder'의 ETF 분석가입니다.
	            제공된 **[테마 카테고리]**와 **[위험 등급]** 정보를 종합하여 분석 결과를 JSON으로 제공하세요.
	            
	            [판단 기준표 (엄격 준수)]
		        1. **성장성 (Growth)**
		           - 매우 높음: 2차전지, AI/로봇, 파생/레버리지
		           - 높음: 반도체, 바이오, 우주/방산, IT/테크, 에너지, 소비재
		           - 보통: 시장대표, 자산배분, 배당, 원자재
		           - 낮음: 금융, 채권/금리, 리츠/부동산
		           
		        2. **배당수익 (Dividend)**
		           - 매우 높음: 리츠/부동산
		           - 높음: 채권/금리, 금융, 배당
		           - 보통: 시장대표, 자산배분, 우주/방산, 반도체
		           - 낮음: 대부분의 성장 테마 (2차전지, AI, 바이오, IT 등)
		           - 없음: 파생/레버리지, 원자재(금/은/선물)
	            
	            [지침]
	            1. **Summary (한 줄 요약)**: 
	               - ETF의 핵심 투자 대상을 20자 내외로 명확하게 정의. (예: "미국 우량 기술주 10개에 집중 투자")
	               
	            2. **Description (상세 설명)**:
	               - 3문장 내외, 친근한 해요체(~해요).
	               - **반드시 [위험 등급]을 고려하여 작성.** (예: 고위험이면 "수익성이 높지만 변동성에 유의하세요", 저위험이면 "안정적으로 자산을 지킬 수 있어요")
	               - 카테고리 특성을 반영하여 초보자가 이해하기 쉽게 설명.
	               
	            3. **RecommendTag (추천 태그)**:
	               - 해당 ETF에 어울리는 키워드 3개 (해시태그 #).
	               - 예: #공격형 #성장주 #배당수익 #안전제일   
	               
	            2. **성장성 (growthLevel), 배당수익 (dividendLevel) 지표 판단 (DB 정보가 없는 것만 추론)**:
	               - 위 기준표에 맞춰 정확한 단어로 답변.
	               - 값은 ["매우 높음", "높음", "보통", "낮음", "없음"] 중 택 1.
	            """;

	        String userMsg = String.format("""
	            [분석 요청 데이터]
	            - 종목명: %s (%s)
	            - **테마 카테고리**: %s
	            - **위험 등급**: %d등급 (%s)
	            
	            위 정보를 바탕으로 summary, description, growthLevel, dividendLevel, recommendTag를 포함한 JSON을 완성해줘.
	            """, etfName, etfCode, category, riskRating, riskContext);

	        return chatClient.prompt()
	                .system(systemMsg)
	                .user(userMsg)
	                .call()
	                .entity(EtfAiDescriptionResponse.class);
	}

}
