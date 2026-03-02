package com.etfinder.mvc.auth.security;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 헤더에서 토큰 꺼내기
        String authorizationHeader = request.getHeader("Authorization");

        // 2. 토큰이 있고, "Bearer "로 시작하는지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // "Bearer " 글자 잘라내고 순수 토큰만 남김
            String token = authorizationHeader.substring(7);

            // 3. 토큰이 유효한지 검사
            if (jwtProvider.validateToken(token)) {
                
                // 4. 토큰에서 ID(providerId) 꺼내기
                String providerId = jwtProvider.getProviderId(token);
                
                // 5. 토큰에서 role 꺼내기
                String role = jwtProvider.getRole(token);
                
                // 권한이 없으면 기본값 USER로 설정
                if (role == null) role = "ROLE_USER";
                
                // 5. 추출한 role을 스프링 시큐리티가 이해하는 형태(List<GrantedAuthority>)로 변환
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

                // 6. 인증절차 (Authentication 객체 생성)
                // 비밀번호는 없으니까 null, 권한은 authorities로 설정
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(providerId, null, authorities);

                // 7. 스프링 시큐리티 저장소에 저장 (컨트롤러에서 식별 가능하도록)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 7. 다음 필터로 넘겨주기
        filterChain.doFilter(request, response);
    }
}