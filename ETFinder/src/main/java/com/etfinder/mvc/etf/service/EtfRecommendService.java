package com.etfinder.mvc.etf.service;

import java.util.List;

import com.etfinder.mvc.etf.dto.EtfRecommendResponse;

public interface EtfRecommendService {

	// etf 추천 기능
	List<EtfRecommendResponse> recommend(String providerId);
}
