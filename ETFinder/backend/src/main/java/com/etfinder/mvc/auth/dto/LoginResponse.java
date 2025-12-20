package com.etfinder.mvc.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken; // JWT 토큰

    @JsonProperty("isNewMember")
    private boolean isNewMember; // ★ 신규 회원 여부 (true/false)

    private String nickname; // 프론트에서 바로 보여줄 닉네임
}