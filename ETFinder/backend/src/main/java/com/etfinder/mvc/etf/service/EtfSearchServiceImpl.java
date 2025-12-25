package com.etfinder.mvc.etf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfAiDescriptionResponse;
import com.etfinder.mvc.etf.dto.EtfDailyHistory;
import com.etfinder.mvc.etf.dto.EtfHolding;
import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.like.mapper.LikeMapper;
import com.etfinder.mvc.ranking.mapper.ViewRankingMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EtfSearchServiceImpl implements EtfSearchService {

	private final EtfMapper etfMapper;
    private final LikeMapper likeMapper; // 새로 주입
    private final ViewRankingMapper viewRankingMapper;
    private final EtfAiService etfAiService;
    private final ObjectMapper objectMapper;

	public EtfSearchServiceImpl(EtfMapper etfMapper, LikeMapper likeMapper, ViewRankingMapper viewRankingMapper,
			EtfAiService etfAiService, ObjectMapper objectMapper) {
		this.etfMapper = etfMapper;
		this.likeMapper = likeMapper;
		this.viewRankingMapper = viewRankingMapper;
		this.etfAiService = etfAiService;
		this.objectMapper = objectMapper;
	}

	// 1. ETF 전체 조회 
	@Override
	public List<EtfProduct> selectAllEtf() {
		
		return etfMapper.selectAllEtf();
	}

	
	// 2. ETF 상세 조회 
    @Override
    public EtfProduct selectOneEtf(Long etfId, Long userId) {
        EtfProduct etf = etfMapper.selectOneEtf(etfId);
        if (etf == null) return null;
        
        // 로그인 안 했으면 무조건 false
        if (userId == null) {
            etf.setLikedByMe(false);
            return etf;
        }

        // 로그인 했으면, 내가 좋아요 눌렀는지 체크
        boolean liked = likeMapper.existsLike(userId, etfId);
        etf.setLikedByMe(liked);
        return etf;
    }

    // 3. ETF 검색 및 정렬 
	@Override
	public List<EtfProduct> searchByCondition(SearchCondition con) {
		return etfMapper.searchByCondition(con);
	}

	// 4. ETF 상세 페이지 진입 시 조회수 증가 
	@Override
	@Transactional
	public int increaseViewCount(Long etfId) {
		
		int updated = viewRankingMapper.increaseViewCount(etfId);
		
		if(updated == 0)
			viewRankingMapper.insertInitial(etfId);
		
		return updated;
	}

	// 5. ETF 상세 페이지 내 ai 설명 없으면 생성 후 저장
	@Override
	public EtfAiDescriptionResponse updateEtfDescription(Long etfId) {
		EtfProduct etf = etfMapper.selectOneEtf(etfId);
		if (etf == null) throw new RuntimeException("ETF 없음");

	    // 1. 이미 DB에 설명이 있으면 바로 파싱해서 리턴
	    if (etf.getDescription() != null && !etf.getDescription().trim().isEmpty()) {
	        try {
	            return objectMapper.readValue(etf.getDescription(), EtfAiDescriptionResponse.class);
	        } catch (Exception e) {
	            log.error("JSON 파싱 에러", e);
	        }
	    }

	    // 2. 설명이 없으면 AI 생성 시작
	    try {
	        log.info("AI 설명 생성 시작: {}", etf.getEtfName());
	        
	        List<EtfHolding> holdings = etfMapper.selectHoldingsByEtfId(etfId);
	        
	        // 상위 3개 종목명만 추출 (데이터 없으면 빈 리스트가 됨)
	        List<String> top3Names = new ArrayList<>();
	        if (holdings != null && !holdings.isEmpty()) {
	            top3Names = holdings.stream()
	                    .limit(3) // 상위 3개만
	                    .map(EtfHolding::getStockName) // 이름만 뽑기
	                    .toList();
	        }
	        
	        log.info(">>> AI로 넘기는 종목 리스트: {}", top3Names);
	        
	        EtfAiDescriptionResponse aiRes = etfAiService.generateDescription(
	            etf.getEtfName(), 
	            etf.getEtfCode(), 
	            etf.getTheme(),
	            etf.getRiskRating(),
	            top3Names
	        );

	        // DB 저장
	        String jsonDescription = objectMapper.writeValueAsString(aiRes);
	        etfMapper.updateEtfDescription(etf.getEtfCode(), jsonDescription);
	        
	        return aiRes;

	    } catch (Exception e) {
	        log.error("AI 생성 실패", e);
	        // 실패 시 빈 껍데기라도 리턴하거나 에러 던짐
	        return new EtfAiDescriptionResponse("분석 중...", "잠시 후 다시 시도해주세요.", "보통", "보통", "#분석대기");
	    }
	}

	// 6. ETF 상세 페이지 내 구성종목 조회
	@Override
	public List<EtfHolding> selectHoldingsByEtfId(Long etfId) {
		List<EtfHolding> list = etfMapper.selectHoldingsByEtfId(etfId);
		
		// 구성종목 정보가 없으면 빈 리스트 반환
		if(list == null)
			return new ArrayList<>();
		
		return list;
	}

	// 7. ETF 상세 페이지 내 차트 조회용 과거 시세 조회
	@Override
	public List<EtfDailyHistory> selectDailyHistory(Long etfId) {
		return etfMapper.selectDailyHistory(etfId);
	}
	
}
