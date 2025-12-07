package com.etfinder.mvc.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.dto.UserUpdateRequest;

@Mapper
public interface UserMapper {

	// 사용자 등록
	void insertUser(User user);

	// 사용자 정보 조회
	User findByProviderId(String providerId);

	// 사용자 정보 조회 (개발자 용)
	User getUserByProviderId(String providerId);

	// 사용자 정보 수정
	int updateUser(@Param("providerId") String providerId, @Param("request") UserUpdateRequest request);

	// 사용자 삭제
	int deleteUser(String providerId);
}
