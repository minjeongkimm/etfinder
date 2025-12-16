package com.etfinder.mvc.etf.service;

import java.util.List;

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
			int riskRating, List<String> topHoldings) {
		
		// 위험 등급을 AI가 이해하기 쉬운 멘트로 변환
		String riskContext = switch (riskRating) {
	        case 1 -> "매우 높은 위험 (초고위험)";
	        case 2 -> "높은 위험 (고위험)";
	        case 3 -> "다소 높은 위험 (중위험)";
	        case 4 -> "낮은 위험 (저위험)";
	        default -> "매우 낮은 위험 (초저위험)";
	    };
	    
	    // 구성종목 유무에 따른 프롬프트 분기 처리
        boolean hasHoldings = (topHoldings != null && !topHoldings.isEmpty());
        
        String descriptionRule;
        String holdingDataString;

        if (hasHoldings) {
            // [국내/데이터 있음]: 종목명 나열
            String joinedNames = String.join(", ", topHoldings);
            holdingDataString = "- **주요 구성종목**: " + joinedNames;
            
            // 프롬프트 규칙: 종목명 포함 지시
            descriptionRule = """
               ✅ **필수 포함**: 제공된 **'주요 구성종목'(%s 등)**의 이름을 설명에 자연스럽게 녹여내세요.
               (예: "삼성전자, SK하이닉스 같은 대표 반도체 기업에 투자하여...")
               """.formatted(joinedNames);
        } else {
            // [해외/데이터 없음]: 종목명 금지
            holdingDataString = "- **주요 구성종목**: 데이터 없음 (해외 지수 추종 등)";
            
            // 프롬프트 규칙: 종목명 금지 지시
            descriptionRule = """
               🚫 **특정 기업명/종목명 언급 절대 금지**: 삼성전자, 애플, 테슬라 등 구체적인 회사 이름을 절대 거론하지 마세요. (데이터가 없으므로 추측 금지)
               ✅ **대체 표현**: 기업 이름 대신 **'주요 대형주', 'IT 대표 기업들', '우량한 금융사들'** 처럼 **섹터나 업종의 특성**으로 설명하세요.
               """;
        }
	    
	    String systemMsg = """
	            당신은 'ETFinder'의 ETF 분석가입니다. 제공된 **[테마 카테고리]**와 **[위험 등급]**을 기준(Strict Rule)으로 JSON 결과를 생성하세요.
    
			    [1. 지표 판단 기준표 (DB 미보유 시 추론용)]
			    * **성장성**: [매우높음] 2차전지,AI,레버리지 / [높음] 반도체,바이오,방산,IT,에너지 / [보통] 시장대표,자산배분,원자재 / [낮음] 금융,채권,리츠
			    * **배당수익**: [매우높음] 리츠 / [높음] 채권,금융,배당주 / [보통] 시장대표,반도체 / [낮음] 성장테마(AI,바이오 등) / [없음] 레버리지,원자재
			    (값은 '매우 높음', '높음', '보통', '낮음', '없음' 중 택1)
			
	    		[2. 말투 가이드라인 (절대 준수)]
			    * **무조건 '해요체'를 사용하세요.** (~~합니다, ~~습니다 금지 ❌)
			    * 나쁜 예: "수익을 기대할 수 있습니다."
			    * **좋은 예**: "수익을 기대할 수 **있어요.**", "변동성에 주의해야 **해요.**"
			    
			    [3. 작성 지침]
			    A. **Risk 표현**: 기계적 경고 금지. 테마별 맞춤 표현 사용.
			       - 시장지수: "시장 흐름에 따른 등락", "장기 성장"
			       - 채권/금리: "안정적", "자산 방어"
			       - 레버리지/2차전지: 기존대로 "변동성 유의" 유지.
			    B. **용어/문맥**:
			       - 채권/리츠: '배당' 대신 **'정기 분배금', '이자 수익'** 사용.
			       - 해외형: **'환율 변동', '글로벌 분산'** 효과 언급.
			    C. **Summary**: 20자 내외, 상품명 반복 없이 **투자 대상** 명확화 (예: "미국 우량 기술주 10선"). 명사형으로 끝내기, 문장형 종결 금지
			    D. **Description**: 3문장 내외. 위험등급 반영.
			       %s
			       - **인버스/레버리지**: "지수를 (역으로) 추종한다"는 구조적 특징을 설명하세요.
			    E. **Tags**: 3개. 부정적 단어 순화 (#저위험→#안정지향). 모순 금지.
			       - **시장대표**: #공격투자 태그 금지 -> 대신 **#장기투자, #시장대표, #꾸준함, #포트폴리오핵심** 사용.
	    		   - **채권/금리**: #이자수익, #자산방어, #안정지향
	    		   - 인버스/레버리지: #하락장대비, #단기투자, #변동성유의
			    """.formatted(descriptionRule);

	        String userMsg = String.format("""
	            [분석 요청 데이터]
	            - 종목명: %s (%s)
	            - **테마 카테고리**: %s
	            - **위험 등급**: %d등급 (%s)
	            %s
	            
	            위 정보를 바탕으로 summary, description, growthLevel, dividendLevel, recommendTag를 포함한 JSON을 완성해줘.
	            """, etfName, etfCode, category, riskRating, riskContext, holdingDataString);

	        return chatClient.prompt()
	                .system(systemMsg)
	                .user(userMsg)
	                .call()
	                .entity(EtfAiDescriptionResponse.class);
	}

}
