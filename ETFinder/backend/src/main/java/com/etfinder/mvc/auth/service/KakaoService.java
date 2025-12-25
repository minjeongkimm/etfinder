package com.etfinder.mvc.auth.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.etfinder.mvc.auth.dto.KakaoUser;
import com.etfinder.mvc.auth.dto.LoginResponse;
import com.etfinder.mvc.auth.security.JwtProvider;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KakaoService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Value("${kakao.api_key}")
	private String kakaoApiKey;
	
	@Value("${kakao.redirect_uri}")
	private String kakaoRedirectUri;
	
	private final String ADMIN_PROVIDER_ID = "4604028154";
	
	public LoginResponse kakaoLogin(String code) {
		// 1. 인가 코드로 액세스 토큰 요청
		String accessToken = getAccessToken(code);
		// 2. 액세스 토큰으로 사용자 정보 요청
		KakaoUser kakaoUserInfo = getUserInfo(accessToken);
		// 3. db 조회 후 없으면 회원가입, 있으면 로그인 처리 
		String providerId = String.valueOf(kakaoUserInfo.getId());
        String nickname = kakaoUserInfo.getKakao_account().getProfile().getNickname();
        
        User user = userMapper.findByProviderId(providerId);
        boolean isNewMember = false; // 기본값 false

        // 유저가 없으면 회원가입 (INSERT)
        if (user == null) {
            user = new User();
            user.setProviderId(providerId);
            user.setNickname(nickname);
            user.setProvider("kakao");
            user.setCreatedAt(LocalDateTime.now());
            
            userMapper.insertUser(user); // DB 저장
            isNewMember = true;          // ★ 신규 회원임을 표시
        }
		
        // 4. 관리자 권한 설정
		// 기본은 일반 유저
		String role = "ROLE_USER"; 
        
        // 내 ID랑 똑같으면 관리자 설정
        if (ADMIN_PROVIDER_ID.equals(user.getProviderId())) {
            role = "ROLE_ADMIN";
        }
		
        // 5. JWT 토큰 생성
     	String jwtToken = jwtProvider.createToken(user, role);
     		
     	// 6. DTO에 담아서 반환 (토큰, 신규여부, 닉네임)
     	return new LoginResponse(jwtToken, isNewMember, nickname);
	}
	
	// 액세스 토큰 받기
	private String getAccessToken(String code) {
		// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoApiKey);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON 응답에서 access_token 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        return jsonNode.get("access_token").asText();
	}
	
	// 사용자 정보 받아오기 
	private KakaoUser getUserInfo(String accessToken){
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();

        ResponseEntity<KakaoUser> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                KakaoUser.class
        );

        return response.getBody();
	}
	
}
