package com.etfinder.mvc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.etfinder.mvc.etf.load.EtfLoadService;

@ComponentScan(basePackages = "com.etfinder.mvc")
@MapperScan("com.etfinder.mvc.**.mapper") 
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class EtFinderApplication implements CommandLineRunner { 

    @Autowired
    private EtfLoadService etfLoadService; 

    public static void main(String[] args) {
        SpringApplication.run(EtFinderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 서버 켜질 때 이 부분이 자동으로 실행됨!
//        System.out.println("🚀 데이터 적재 시작...");
//        etfLoadService.load(); 
//        System.out.println("✅ 데이터 적재 완료!");
    }
    
    
}