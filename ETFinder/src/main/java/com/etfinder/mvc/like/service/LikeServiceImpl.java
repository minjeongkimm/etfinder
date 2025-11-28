package com.etfinder.mvc.like.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etfinder.mvc.etf.dto.EtfProduct;

@Service
public class LikeServiceImpl implements LikeService {

	@Override
	public int addLike(Long userId, Long etfId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<EtfProduct> getLikedEtfs(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteLike(Long userId, Long etfId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLikeCount(Long etfId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
