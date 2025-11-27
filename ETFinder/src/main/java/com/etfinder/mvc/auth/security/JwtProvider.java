package com.etfinder.mvc.auth.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.etfinder.mvc.user.dto.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    private final long EXPIRE_TIME = 1000 * 60 * 60; // 1시간

    // 비밀키를 Key 객체로 변환하는 메서드
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성 메서드
    public String createToken(User user) {
        return Jwts.builder()
                .setSubject(user.getProviderId())
                .claim("id", user.getUserId())
                .claim("nickname", user.getNickname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) 
                .compact();
    }
    
    // 토큰에서 사용자 식별자 추출
    public String getProviderId(String token) {
        return parseClaims(token).getSubject();
    }
    
    // 토큰 유효성 검증 & 클레임 파싱용 내부 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 토큰 만료, 위조 등 예외 처리 (나중에 시간남으면 구체적으로 구현)
            e.printStackTrace();
            throw e; 
        }
    }
    
    // 토큰이 유효한지 검사하는 메서드 (Filter에서 사용)
    public boolean validateToken(String token) {
        try {
            // parseClaims가 에러 없이 잘 넘어가면 유효한 토큰임
            parseClaims(token);
            return true;
        } catch (Exception e) {
            // 만료되거나 위조된 토큰이면 에러가 나서 여기로 옴 -> false 반환
            System.out.println("유효하지 않은 토큰입니다: " + e.getMessage());
            return false;
        }
    }
}