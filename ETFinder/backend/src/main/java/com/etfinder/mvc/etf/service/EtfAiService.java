package com.etfinder.mvc.etf.service;

import java.util.List;

import com.etfinder.mvc.etf.dto.EtfAiDescriptionResponse;

public interface EtfAiService {

	EtfAiDescriptionResponse generateDescription(String etfName, String etfCode, String category, int riskRating, List<String> topHoldings);
}
