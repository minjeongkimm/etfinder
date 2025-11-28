package com.etfinder.mvc.like.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.etf.dto.EtfProduct;

@Mapper
public interface LikeMapper {

	// 1. 좋아요 누르기
	int addLike(@Param("userId") Long userId, @Param("etfId") Long etfId);

	/*
	 * 2. 로그인 유저의 좋아요 리스트 조회
	 */
	List<EtfProduct> getLikedEtfs(Long userId);

	/*
	 * 3. 좋아요 해제 (이미 누른 유저만 가능) - 이미 좋아요 누른 것에 대해서만 좋아요 해제 가능 - 성공 시 1, 실패 시 0
	 */
	int deleteLike(@Param("userId") Long userId, @Param("etfId") Long etfId);

	/*
	 * 4. Etf 별 좋아요 수 조회
	 */
	int getLikeCount(Long etfId);
	
	
	/*
	 * 5. 사용자가 이미 좋아요를 눌렀는지 체크
	 */
	boolean existsLike(@Param("userId") Long userId, @Param("etfId") Long etfId);
}
