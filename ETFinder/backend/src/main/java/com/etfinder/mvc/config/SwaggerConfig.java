package com.etfinder.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
 
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
    	// 1. 보안 스키마 설정 (JWT라고 알려주는 것)
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
    	
        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement); // 2. 전체 API에 적용
    }
 
    private Info apiInfo() {
        return new Info()
                .title("ETFinder")
                .description("<h3>ETF 초보자를 위한 맞춤형 투자 큐레이션 서비스</h3>")
                .version("1.0.0");
    }
}
