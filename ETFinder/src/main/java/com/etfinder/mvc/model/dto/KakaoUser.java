package com.etfinder.mvc.model.dto;

import lombok.Data;

@Data
public class KakaoUser {
	// 카카오가 주는 JSON 코드에서 닉네임만 빼오는 클래스 
	
    private Long id; // 회원번호 (필수)
    private KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        private Profile profile;

        @Data
        public static class Profile {
            private String nickname; // 닉네임 (필수)
        }
    }
}