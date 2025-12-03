package com.etfinder.mvc.etf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.etf.dto.EtfProduct;
import com.etfinder.mvc.etf.dto.EtfRecommendResponse;
import com.etfinder.mvc.etf.mapper.EtfMapper;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.mapper.UserMapper;

@Service
public class EtfRecommendServiceImpl implements EtfRecommendService{

	@Autowired
	private EtfMapper etfMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<EtfRecommendResponse> recommend(String providerId) {
		
		
		// 사용자 정보와 etf 정보 가져오기
		User user = userMapper.findByProviderId(providerId);
		
		if (user == null) return Collections.emptyList(); // 유령 회원 방어
		
		List<EtfProduct> etfList = etfMapper.selectEtfsForRecommendation();
		
		// 점수 계산 이후 나온 결과(etf 정보, 점수 정보) 저장할 리스트
		List<EtfRecommendResponse> scoredList = new ArrayList<>();
		
		for (EtfProduct etf : etfList) {
			// 1. 필터링
			if (shouldFilterOut(user, etf))
                continue; 
			
			// 2. 점수 계산
			double score = calculateScore(user, etf);
			
			// 3. 결과 dto로 포장
			EtfRecommendResponse response = new EtfRecommendResponse(etf, score);
			
			// 4. 리스트에 저장
			scoredList.add(response);
		}
		
		// 점수 높은 순으로 정렬 (내림차순)
		Collections.sort(scoredList, new Comparator<EtfRecommendResponse>() {
			@Override
			public int compare(EtfRecommendResponse o1, EtfRecommendResponse o2) {
				return Double.compare(o2.getScore(), o1.getScore());
			}
		});
		
		// 10개 이상일 경우 점수 높은 것 10개만 출력
		if(scoredList.size() > 10)
			return scoredList.subList(0, 10);
		
		return scoredList;
	}
	
	// 필터링 로직
	private boolean shouldFilterOut(User user, EtfProduct etf) {
        // 테마나 성향 정보가 없으면 필터링하지 않음
        if (etf.getTheme() == null || user.getPropensity() == null) return false;

        // 안정형, 중립형 유저에게 "파생/레버리지" 테마가 있다면? -> 무조건 제외(True)
        if ("STABLE".equals(user.getPropensity()) && etf.getTheme().contains("파생"))
            return true;
        
        if ("NEUTRAL".equals(user.getPropensity()) && etf.getTheme().contains("파생")) 
            return true;
        
        return false; // 통과
    }
	
	// 점수 합산
	private double calculateScore(User user, EtfProduct etf) {
		double total = 0;
		total += getPropensityScore(user.getPropensity(), etf.getRiskRating())
				+ getReturnScore(etf.getReturn1yr())
				+ getFeeScore(etf.getFee())
				+ getAgeThemeBonus(user.getAge(), etf.getTheme());
		
		return total;
	}
	
	// 투자 성향에 따른 위험도 점수(40%)
	private double getPropensityScore(String userType, Integer riskRate) {
		if(riskRate == null) return 0;
		
		// 공격형일 경우 1,2등급에 최고점, 3등급에 30점, 그 외에 10점
		if("AGGRESSIVE".equals(userType)) {
			if(riskRate == 1 || riskRate == 2) return 40;
			if(riskRate == 3) return 30;
			else return 10;
		}
		// 안졍형일 경우 4등급에 최고점, 3등급에 20점, 그 외에 0점
		if("STABLE".equals(userType)) {
			if(riskRate == 4) return 40;
			if(riskRate == 3) return 20;
			else return 0;
		}
		else {
			// 중립형일 경우 2,3등급일 경우 최고점, 그 외에 20점
			if(riskRate == 2 || riskRate == 3) return 40;
			else return 20;
		}
	}
	
	// 1년 수익률 점수(30%)
	private double getReturnScore(Double returnRate) {
		if(returnRate == null) return 0;
		
		// 수익률이 30% 이상일 경우 30점, 그 이하는 수익률대로, 음수는 0점 
		if(returnRate >= 30) return 30;
		if(returnRate <= 0) return 0;
		else return returnRate;
	}
	
	// 수수료 점수(20%)
	private double getFeeScore(Double fee) {
		if(fee == null) return 0;
		
		// 수수료 낮을수록 점수 높게, 음수 안 나오도록 계산
		double score = 20 - (fee * 20);
		return score < 0 ? 0: score;
	}
	
	// 나이에 따른 테마 보너스 점수(10%)
	private double getAgeThemeBonus(Integer age, String theme) {
		if(age == null || theme == null) return 0;
		
		double bonus = 0;
		
		//2030 세대: 고성장 기술주에 보너스
		if (age <= 39) {
            if (theme.contains("AI") || 
                theme.contains("반도체") || 
                theme.contains("IT") || theme.contains("테크") || 
                theme.contains("2차전지") || theme.contains("전기차") ||
                theme.contains("우주") || theme.contains("방산") ||   
                theme.contains("바이오") ||                          
                theme.contains("에너지") || theme.contains("소비")) { 
                bonus = 10;
            }
        }
		// 40대: 시장지수, 금융으로 성장과 안정의 밸런스
		else if (age <= 49) {
            if (theme.contains("시장대표") ||
                theme.contains("금융") ||
                theme.contains("자산배분")) { 
                bonus = 10;
            } else if (theme.contains("IT") || theme.contains("테크")) {
                bonus = 5;
            }
        }
		// 50대: 배당, 채권으로 노후 준비 
		else if (age >= 50) {
            if (theme.contains("배당") ||
                theme.contains("채권") || theme.contains("금리") ||
                theme.contains("리츠") || theme.contains("부동산") ||
                theme.contains("자산배분")) {                      
                bonus = 10;
            } else if (theme.contains("시장대표")) {
                bonus = 5;
            }
        }
		return bonus;
	}

}
