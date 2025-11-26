package com.etfinder.mvc.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.etfinder.mvc.model.dto.User;

@Mapper
public interface UserMapper {

	// 사용자 등록 
	void insertUser(User user);
	
	// 사용자를 고유 번호로 찾기
	User findByProviderId(String providerId);
	
	// 사용자 정보 수정
	void updateUser(User user);
}
