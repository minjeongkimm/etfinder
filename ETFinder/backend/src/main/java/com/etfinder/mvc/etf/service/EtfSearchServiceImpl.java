package com.etfinder.mvc.etf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.like.mapper.LikeMapper;
import com.etfinder.mvc.ranking.mapper.ViewRankingMapper;

@Service
public class EtfSearchServiceImpl implements EtfSearchService {

	private final EtfMapper etfMapper;
    private final LikeMapper likeMapper; // 새로 주입
    private final ViewRankingMapper viewRankingMapper;

	
    public EtfSearchServiceImpl(EtfMapper etfMapper, LikeMapper likeMapper, ViewRankingMapper viewRankingMapper) {
		this.etfMapper = etfMapper;
		this.likeMapper = likeMapper;
		this.viewRankingMapper = viewRankingMapper;
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
	public int increaseViewCount(Long etfId) {
		return viewRankingMapper.increaseViewCount(etfId);
	}
}
