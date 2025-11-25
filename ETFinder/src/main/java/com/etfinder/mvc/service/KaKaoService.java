package com.etfinder.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.model.mapper.UserMapper;

@Service
public class KaKaoService {

	@Autowired
	private UserMapper userMapper;
	
	public String kakaoLogin(String code) {
		
		
		return code;
	}
	
}
