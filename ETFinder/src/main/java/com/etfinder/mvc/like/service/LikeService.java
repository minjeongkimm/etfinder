package com.etfinder.mvc.like.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.etf.dto.EtfProduct;

public interface LikeService {

	
	/*
	 * 1. 좋아요 누르기 (로그인 유저만 가능)
	 * - like 안에 userId, etfId 넘김
	 * - 성공 시 1, 실패 시 0
	 */
	int addLike(Long userId, Long etfId);

	/*
	 * 2. 로그인 유저의 좋아요 리스트 조회
	 */
	List<EtfProduct> getLikedEtfs(Long userId);
	

	/*
	 * 3. 좋아요 해제 (로그인 유저만 가능)
	 * - 이미 좋아요 누른 것에 대해서만 좋아요 해제 가능
	 * - 성공 시 1, 실패 시 0
	 */
	int deleteLike(Long userId, Long etfId);
	
	/*
	 * 4. 사용자가 이미 좋아요 눌렀는지 체크
	 */
	
	boolean existsLike(Long userId, Long etfId);
	
}
