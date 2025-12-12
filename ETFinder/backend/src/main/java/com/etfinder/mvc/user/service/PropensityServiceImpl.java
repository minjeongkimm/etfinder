package com.etfinder.mvc.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.user.constant.InvestmentType;
import com.etfinder.mvc.user.dto.PropensityAiResponse;
import com.etfinder.mvc.user.dto.PropensityRequest;
import com.etfinder.mvc.user.dto.PropensityResult;
import com.etfinder.mvc.user.dto.UserUpdateRequest;
import com.etfinder.mvc.user.mapper.UserMapper;

@Service
public class PropensityServiceImpl implements PropensityService{
	
	private final UserMapper userMapper;
	private final PropensityAiService propensityAiService;

	public PropensityServiceImpl(UserMapper userMapper, PropensityAiService propensityAiService) {
		this.userMapper = userMapper;
		this.propensityAiService = propensityAiService;
	}

	@Override
	public PropensityResult analyze(String providerId, PropensityRequest request) {
		
		// 점수 계산 로직
		int totalScore = 0;
		List<Integer> answers = request.getAnswers();
		if(answers != null) {
			for (int answer : request.getAnswers()) {
				totalScore += answer;	// 프론트에서 각 번호당 값을 10, 20, 30, 40으로 반환하도록 설정 -> 반환된 값을 그냥 더하면 됨
			}
		}
		
		// 점수로 성향 판별
		InvestmentType type = InvestmentType.findByScore(totalScore);
		
		// 얻은 성향 값 db에 업데이트
		UserUpdateRequest updateRequest = new UserUpdateRequest();
		updateRequest.setPropensity(type.name());
		userMapper.updateUser(providerId, updateRequest);
		
		// ai 분석
		// (1) 숫자 답변([10, 40...])을 텍스트로 변환 (아래 메서드 호출)
        String surveySummary = translateAnswersToText(answers);
        
        // (2) AI에게 요청 (label 사용: "안정형" 등)
        PropensityAiResponse aiRes = propensityAiService.analyzePropensity(type.getLabel(), surveySummary);
		
		// 결과값 반환
		return new PropensityResult(type, aiRes);
	}
	
	private String translateAnswersToText(List<Integer> answers) {
        if (answers == null || answers.size() < 5) return "정보 없음";
        StringBuilder sb = new StringBuilder();

        // 사용자가 제공한 문항 점수에 따라 텍스트로 매핑
     // Q1. 투자 경험
        int q1 = answers.get(0);
        sb.append("- 투자 경험: ");
        if (q1 == 10) sb.append("없음(예적금만 함)");
        else if (q1 == 20) sb.append("초보(펀드/ETF 경험)");
        else if (q1 == 30) sb.append("중수(주식 직접 투자, 용어 앎)");
        else sb.append("고수(파생상품/코인 경험 있음)");
        sb.append("\n");

        // Q2. 투자 기간
        int q2 = answers.get(1);
        sb.append("- 자금 성격(기간): ");
        if (q2 == 10) sb.append("1년 이내(단기, 급한 돈)");
        else if (q2 == 20) sb.append("1~3년(중단기)");
        else if (q2 == 30) sb.append("3~5년(여유 자금)");
        else sb.append("5년 이상(장기/노후 자금)");
        sb.append("\n");

        // Q3. 위험 감수성 (-20% 하락 시)
        int q3 = answers.get(2);
        sb.append("- 하락장(-20%) 반응: ");
        if (q3 == 10) sb.append("멘붕(잠 안 옴/손절)");
        else if (q3 == 20) sb.append("불안(관망하다 더 떨어지면 매도)");
        else if (q3 == 30) sb.append("존버(언젠가 오르겠지)");
        else sb.append("야수의 심장(추가 매수 기회!)");
        sb.append("\n");

        // Q4. 기대 수익률
        int q4 = answers.get(3);
        sb.append("- 목표 수익률: ");
        if (q4 == 10) sb.append("원금 보전 중심(3~5%)");
        else if (q4 == 20) sb.append("물가 상승률 방어(6~9%)");
        else if (q4 == 30) sb.append("시장 평균 수익(10~15%)");
        else sb.append("고수익 대박(20% 이상)");
        sb.append("\n");

        // Q5. 수입/자산 현황
        int q5 = answers.get(4);
        sb.append("- 재무 상황: ");
        if (q5 == 10) sb.append("소득 불안정/여유 없음");
        else if (q5 == 20) sb.append("고정 소득 있으나 여유 적음");
        else if (q5 == 30) sb.append("월급/여유 자금 운용 중");
        else sb.append("소득 안정/자산 충분함");
        sb.append("\n");

        return sb.toString();
    }

}
