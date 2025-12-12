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
        // Q1 (인덱스 0)
        int q1 = answers.get(0);
        sb.append("1.투자경험: ").append(q1 == 10 ? "없음" : q1 == 20 ? "간접투자" : q1 == 30 ? "직접투자" : "파생상품경험").append("\n");
        
        // Q2 (인덱스 1)
        int q2 = answers.get(1);
        sb.append("2.투자기간: ").append(q2 == 10 ? "1년미만" : q2 == 20 ? "1~3년" : q2 == 30 ? "3~5년" : "5년이상").append("\n");

        // Q3 (인덱스 2)
        int q3 = answers.get(2);
        sb.append("3.위험반응(-20%): ").append(q3 == 10 ? "매도(손절)" : q3 == 20 ? "관망" : q3 == 30 ? "존버" : "추가매수").append("\n");

        // Q4 (인덱스 3)
        int q4 = answers.get(3);
        sb.append("4.목표수익: ").append(q4 == 10 ? "원금보전" : q4 == 20 ? "물가상승률" : q4 == 30 ? "시장수익률" : "고수익").append("\n");

        // Q5 (인덱스 4)
        int q5 = answers.get(4);
        sb.append("5.자산현황: ").append(q5 == 10 ? "여유없음" : q5 == 20 ? "소액" : q5 == 30 ? "보통" : "여유있음").append("\n");

        return sb.toString();
    }

}
