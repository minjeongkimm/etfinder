package com.etfinder.mvc.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.auth.service.KakaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

	private final KakaoService kakaoService;
	
	@GetMapping("/kakao/callback")
	public ResponseEntity<?> kakaoCallBack(@RequestParam String code){
		String jwtToken = kakaoService.kakaoLogin(code);
		Map<String, Object> response = new HashMap<>();
        response.put("accessToken", jwtToken);
        response.put("message", "Login Success");
		return ResponseEntity.ok(response);
	}
	
	
	
	
	
	
}
