package com.etfinder.mvc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.etfinder.mvc.etf.load.EtfHoldingsLoadService;
import com.etfinder.mvc.etf.load.EtfLoadService;

@ComponentScan(basePackages = "com.etfinder.mvc")
@MapperScan("com.etfinder.mvc.**.mapper") 
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class EtFinderApplication implements CommandLineRunner { 

    @Autowired
    private EtfLoadService etfLoadService; 
    
    @Autowired
    private EtfHoldingsLoadService etfHoldingsLoadService;

    public static void main(String[] args) {
        SpringApplication.run(EtFinderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. 기본 ETF 정보 적재
//        System.out.println("🚀 데이터 적재 시작...");
//        etfLoadService.load(); 
//        System.out.println("✅ 데이터 적재 완료!");
    	// 2. 구성종목(Holdings) 엑셀 파일 적재
//        System.out.println("🚀 [Holdings] ETF 구성종목 엑셀 적재 시작...");
//        etfHoldingsLoadService.loadHoldings(); 
//        System.out.println("✅ [Holdings] ETF 구성종목 적재 완료!");
    }
    
    
}