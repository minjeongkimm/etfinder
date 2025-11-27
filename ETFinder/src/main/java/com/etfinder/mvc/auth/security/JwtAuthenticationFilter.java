package com.etfinder.mvc.auth.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

                // 5. "이 사람 인증됐어요!" 도장 쾅! (Authentication 객체 생성)
                // 비밀번호는 없으니까 null, 권한은 일단 비워둠(Collections.emptyList())
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(providerId, null, Collections.emptyList());

                // 6. 스프링 시큐리티 저장소에 저장 (컨트롤러에서 식별 가능하도록)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 7. 다음 필터로 넘겨주기
        filterChain.doFilter(request, response);
    }
}