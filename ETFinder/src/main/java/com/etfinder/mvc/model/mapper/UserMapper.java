package com.etfinder.mvc.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.etfinder.mvc.model.dto.User;

@Mapper
public interface UserMapper {

	void insertUser(User user);
	
	User findByProviderId(String providerId);
	
	void updateUser(User user);
}
