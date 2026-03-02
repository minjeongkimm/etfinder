package com.etfinder.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.etfinder.mvc.EtFinderApplication;
import com.etfinder.mvc.auth.security.JwtProvider;
import com.etfinder.mvc.user.dto.User;

@SpringBootTest(classes = EtFinderApplication.class) // 스프링 컨테이너를 띄워서 @Value 값 등을 가져오게 함
public class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void 토큰_생성_및_검증_테스트() {
        // 1. 가짜 유저 데이터 만들기
        User fakeUser = new User();
        fakeUser.setProviderId("123456789"); // 카카오 회원번호라고 치고
        fakeUser.setNickname("테스트개미");

        // 2. 토큰 생성해보기 (createToken)
        String token = jwtProvider.createToken(fakeUser, "ROLE_USER");
        
        System.out.println("============================================");
        System.out.println("생성된 토큰: " + token);
        System.out.println("============================================");

        // 3. 토큰이 null이 아니어야 함
        assertThat(token).isNotNull();

        // 4. 토큰에서 다시 아이디 꺼내보기 (getUserIdFromToken)
        String userId = jwtProvider.getProviderId(token);
        
        System.out.println("토큰에서 꺼낸 ID: " + userId);

        // 5. 처음에 넣은 ID랑 꺼낸 ID가 똑같은지 확인
        assertThat(userId).isEqualTo("123456789");
    }
}