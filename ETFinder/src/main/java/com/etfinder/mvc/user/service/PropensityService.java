package com.etfinder.mvc.user.service;

import com.etfinder.mvc.user.dto.PropensityRequest;
import com.etfinder.mvc.user.dto.PropensityResult;

public interface PropensityService {

	// 성향 테스트 후 성향 분석 
	PropensityResult analyze(String providerId, PropensityRequest request);
	
}
