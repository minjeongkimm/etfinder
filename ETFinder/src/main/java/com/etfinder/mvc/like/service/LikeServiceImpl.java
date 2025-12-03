package com.etfinder.mvc.like.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.like.mapper.LikeMapper;

@Service
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;
	
	public LikeServiceImpl(LikeMapper likeMapper) {
		this.likeMapper = likeMapper;
	}

	// 1. 좋아요 누르기
    @Transactional
    @Override
    public int addLike(Long userId, Long etfId) {
        if (likeMapper.existsLike(userId, etfId))
            return 0;

        int rows = likeMapper.addLike(userId, etfId);
        likeMapper.increaseLikeCount(etfId);

        return rows;
    }

	// 2. 로그인 유저의 좋아요 리스트 조회 
	@Override
	public List<EtfProduct> getLikedEtfs(Long userId) {
		return likeMapper.getLikedEtfs(userId);
	}

	// 3. 좋아요 해제 (이미 누른 유저만 가능)
	@Transactional
    @Override
    public int deleteLike(Long userId, Long etfId) {
        if (!likeMapper.existsLike(userId, etfId))
            return 0;

        int rows = likeMapper.deleteLike(userId, etfId);
        likeMapper.decreaseLikeCount(etfId);

        return rows;
    }


	// 4. 사용자가 이미 좋아요를 눌렀는지 체크 
	@Override
	public boolean existsLike(Long userId, Long etfId) {
		return likeMapper.existsLike(userId, etfId);
	}


}
