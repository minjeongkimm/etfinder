package com.etfinder.mvc.user.service;

import com.etfinder.mvc.user.dto.PropensityAiResponse;

public interface PropensityAiService {

	PropensityAiResponse analyzePropensity(String type, String summary);
}
