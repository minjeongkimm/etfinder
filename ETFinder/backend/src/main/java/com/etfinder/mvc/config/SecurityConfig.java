package com.etfinder.mvc.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.etfinder.mvc.auth.security.JwtAuthenticationFilter;
import com.etfinder.mvc.auth.security.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
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
            
            // [추가] CORS 설정 연결하기
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); // 프론트엔드 주소 허용
                    config.setAllowedMethods(Collections.singletonList("*")); // GET, POST, PUT 등 모든 메서드 허용
                    config.setAllowCredentials(true); // 쿠키, 인증 헤더 허용
                    config.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
                    config.setMaxAge(3600L); // 1시간 동안 캐싱
                    return config;
                }
            }))
            
            // 2. Form 로그인 해제 (카카오 로그인 사용하기 때문에)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // 3. 세션 사용 안 함 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 4. URL별 권한 관리
            .authorizeHttpRequests(auth -> auth
            		// etf 등록, 수정, 삭제는 관리자만 가능
	            	.requestMatchers(HttpMethod.POST, "/api/etfs/**").hasRole("ADMIN")
	                .requestMatchers(HttpMethod.PATCH, "/api/etfs/**").hasRole("ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/api/etfs/**").hasRole("ADMIN")
	                // etf 조회는 누구나 가능 
	                .requestMatchers(HttpMethod.GET, "/api/etfs/**").permitAll()
	                // 누구나 접속 가능
	                .requestMatchers(
	                        "/", 
	                        "/index.html", 
	                        "/api/auth/**",      // 로그인 관련은 누구나 접속 가능
	                        "/api/ranking/**",		// 랭킹도 로그인 없이 조회 가능 
	                        "/swagger-ui/**", "/v3/api-docs/**"		// 스웨거 화면 접속용
	                ).permitAll()
	                .requestMatchers("/api/likes/**").authenticated()
	                .requestMatchers("/api/bookmarks/**").authenticated()
	                .requestMatchers("/api/comments/**").authenticated()
	                .anyRequest().authenticated() // 그 외 모든 요청(내 정보 수정 등)은 토큰 필요!
            )

            // 5. 만든 JWT 필터를 "UsernamePassword...Filter" 앞에 끼워넣기
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}