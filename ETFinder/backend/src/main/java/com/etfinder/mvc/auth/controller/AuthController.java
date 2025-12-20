package com.etfinder.mvc.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.auth.dto.LoginResponse;
import com.etfinder.mvc.auth.service.KakaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

	private final KakaoService kakaoService;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<LoginResponse> kakaoCallBack(@RequestParam String code){
		LoginResponse response = kakaoService.kakaoLogin(code);
		return ResponseEntity.ok(response);
	}
	
}
