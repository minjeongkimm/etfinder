package com.etfinder.mvc.etf.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.SearchCondition;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.like.mapper.LikeMapper;

@Service
public class EtfSearchServiceImpl implements EtfSearchService {

	private final EtfMapper etfMapper;
    private final LikeMapper likeMapper; // 새로 주입

    public EtfSearchServiceImpl(EtfMapper etfMapper, LikeMapper likeMapper) {
        this.etfMapper = etfMapper;
        this.likeMapper = likeMapper;
    }
	
	@Override
	public List<EtfProduct> selectAllEtf() {
		
		return etfMapper.selectAllEtf();
	}

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

	@Override
	public List<EtfProduct> searchByCondition(SearchCondition con) {
		return etfMapper.searchByCondition(con);
	}

}
