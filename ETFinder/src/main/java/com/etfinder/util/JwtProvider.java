package com.etfinder.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.etfinder.mvc.model.dto.User;

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
    public String getUserIdFromToken(String token) {
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
}