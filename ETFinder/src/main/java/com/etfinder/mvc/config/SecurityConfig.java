package com.etfinder.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.etfinder.mvc.auth.security.JwtAuthenticationFilter;
import com.etfinder.mvc.auth.security.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider; 

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 해제
            .csrf(csrf -> csrf.disable())
            
            // 2. Form 로그인 해제 (카카오 로그인 사용하기 때문에)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // 3. 세션 사용 안 함 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 4. URL별 권한 관리
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/", 
                        "/index.html", 
                        "/api/auth/**",      // 로그인 관련은 누구나 접속 가능
                        "/api/etfs/**",        // ETF 조회도 로그인 없이 보여주기
                        "/swagger-ui/**", "/v3/api-docs/**"		// 스웨거 화면 접속용
                ).permitAll()
                .anyRequest().authenticated() // 그 외 모든 요청(내 정보 수정 등)은 토큰 필요!
            )

            // 5. 만든 JWT 필터를 "UsernamePassword...Filter" 앞에 끼워넣기
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}