package com.etfinder.mvc.user.service;

import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.dto.UserResponse;
import com.etfinder.mvc.user.dto.UserUpdateRequest;

public interface UserService {
	
	// 사용자 정보 조회
	UserResponse findByProviderId(String providerId);
	
	// 사용자 정보 조회 (개발자 용)
	User getUserByProviderId(String providerId);

	// 사용자 정보 업데이트
	int updateUser(String providerId, UserUpdateRequest request);
	
	// 사용자 정보 삭제
	int deleteUser(String providerId);
}
