package com.etfinder.mvc.etf.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfAiDescriptionResponse;
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
        
        // ai 설명이 없으면 생성 후 저장
        if (etf.getDescription() == null || etf.getDescription().trim().isEmpty()) {
            try {
                log.info("AI 설명 생성 시작: {}", etf.getEtfName());

                // 1) AI에게 요청 (이름, 코드, 카테고리, 위험등급)
                EtfAiDescriptionResponse aiRes = etfAiService.generateDescription(
                    etf.getEtfName(), 
                    etf.getEtfCode(), 
                    etf.getTheme(), 
                    etf.getRiskRating()
                );
                
                // 2) DTO -> JSON String 변환
                String jsonDescription = objectMapper.writeValueAsString(aiRes);
                
                // 3) DB 및 현재 객체 업데이트
                etfMapper.updateEtfDescription(etf.getEtfCode(), jsonDescription); // DB 저장
                etf.setDescription(jsonDescription); // 화면에 보여줄 객체에도 세팅

                log.info("AI 설명 저장 완료");

            } catch (Exception e) {
                log.error("AI 설명 생성 실패: {}", e.getMessage());
                // 실패해도 상세 페이지는 보여줘야 하므로 예외를 던지지 않고 넘어감
            }
        }

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
}
