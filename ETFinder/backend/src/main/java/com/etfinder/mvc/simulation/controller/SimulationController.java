package com.etfinder.mvc.simulation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.simulation.dto.SimulationRequest;
import com.etfinder.mvc.simulation.dto.SimulationResponse;
import com.etfinder.mvc.simulation.service.SimulationService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

	private final SimulationService simulationService;
	
	private final UserService userService;
	
	public SimulationController(SimulationService simulationService, UserService userService) {
		this.simulationService = simulationService;
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<?> simulate(@AuthenticationPrincipal String providerId, @RequestBody SimulationRequest request){
		
		// 1) 로그인 여부 확인
        if (providerId == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        // 2) providerId → userId 조회
        User user = userService.getUserByProviderId(providerId);
        if (user == null) {
            return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        
        // 3) 시뮬레이션 결과 조회
        long userId = user.getUserId();
        SimulationResponse response = simulationService.simulate(userId, request);
        
        if(response != null) {
        	return new ResponseEntity<SimulationResponse>(response, HttpStatus.OK);
        }else {
        	return new ResponseEntity<String>("수익률 계산에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
	}
	
}
