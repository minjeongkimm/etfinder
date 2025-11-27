package com.etfinder.mvc.user.service;

import com.etfinder.mvc.user.dto.UserResponse;
import com.etfinder.mvc.user.dto.UserUpdateRequest;

public interface UserService {
	
	// 사용자 정보 조회
	UserResponse getUserInfo(String providerId);

	// 사용자 정보 업데이트
	int updateUser(String providerId, UserUpdateRequest request);
	
	// 사용자 정보 삭제
	int deleteUser(String providerId);
}
