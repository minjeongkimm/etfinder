package com.etfinder.mvc.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.dto.UserResponse;
import com.etfinder.mvc.user.dto.UserUpdateRequest;
import com.etfinder.mvc.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	// 사용자 정보 조회
	@Override
	public UserResponse findByProviderId(String providerId) {
		User user = userMapper.findByProviderId(providerId);
		if(user == null) {
			return null;
		}
		
		return new UserResponse(
				user.getEmail(),
				user.getNickname(),
				user.getAge(),
				user.getPropensity(),
				user.getProviderId(),
				user.getCreatedAt()
				);
	}

	
	// 사용자 정보 조회 (개발자 용)
	@Override
	public User getUserByProviderId(String providerId) {
		return userMapper.getUserByProviderId(providerId);
	}
	
	
	// 사용자 정보 업데이트
	@Override
	public int updateUser(String providerId, UserUpdateRequest request) {
		int result = userMapper.updateUser(providerId, request);
		return result;
	}

	// 사용자 정보 삭제
	@Override
	public int deleteUser(String providerId) {
		int result = userMapper.deleteUser(providerId);
		return result;
	}




}
