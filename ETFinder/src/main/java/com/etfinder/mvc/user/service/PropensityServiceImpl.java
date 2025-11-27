package com.etfinder.mvc.user.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.etfinder.mvc.user.constant.InvestmentType;
import com.etfinder.mvc.user.dto.PropensityRequest;
import com.etfinder.mvc.user.dto.PropensityResult;
import com.etfinder.mvc.user.dto.UserUpdateRequest;
import com.etfinder.mvc.user.mapper.UserMapper;

public class PropensityServiceImpl implements PropensityService{
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public PropensityResult analyze(String providerId, PropensityRequest request) {
		
		// 점수 계산 로직
		int totalScore = 0;
		if(request.getAnswers() != null) {
			for (int answer : request.getAnswers()) {
				totalScore += (answer * 10);	// 선택한 번호에 10 곱해서 점수 계산
			}
		}
		
		// 점수로 성향 판별
		InvestmentType type = InvestmentType.findByScore(totalScore);
		
		// 얻은 성향 값 db에 업데이트
		UserUpdateRequest updateRequest = new UserUpdateRequest();
		updateRequest.setPropensity(type.name());
		userMapper.updateUser(providerId, updateRequest);
		
		return new PropensityResult(type);
	}

}
