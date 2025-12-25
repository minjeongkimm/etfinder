package com.etfinder.mvc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {

	// 모든 요청을 잡아다가 헤더를 강제삽입
	@Bean
	public RestClient.Builder restClientBuilder() {
		return RestClient.builder().requestInterceptor((request, body, execution) -> {
			request.getHeaders().set("Content-Type", "application/json");

			return execution.execute(request, body);
		});
	}
	
	// 생성자 주입을 쉽게 받기 위해 미리 설정
	@Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .build();
    }
}
