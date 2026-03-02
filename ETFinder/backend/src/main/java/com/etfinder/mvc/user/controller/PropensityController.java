package com.etfinder.mvc.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.user.dto.PropensityRequest;
import com.etfinder.mvc.user.dto.PropensityResult;
import com.etfinder.mvc.user.dto.UserUpdateRequest;
import com.etfinder.mvc.user.service.PropensityService;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/propensity")
public class PropensityController {

	private final PropensityService propensityService;
	private final UserService userService;
	
	public PropensityController(PropensityService propensityService, UserService userService) {
		this.propensityService = propensityService;
		this.userService = userService;
	}

	// 성향 테스트 후 점수 계산해서 결과 db에 저장, 프론트로 바로 보내기
	@PostMapping("/test")
	public ResponseEntity<?> submitPropensity(@AuthenticationPrincipal String providerId, @RequestBody PropensityRequest request){
		PropensityResult result = propensityService.analyze(providerId, request);
		
		if(result != null) {
			return new ResponseEntity<PropensityResult>(result, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("결과 분석에 실패했습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/simple")
	public ResponseEntity<String> simplePropensity(@AuthenticationPrincipal String providerId,@RequestBody UserUpdateRequest request){
		userService.updateUser(providerId, request);
		return new ResponseEntity<String>("성향이 업데이트되었습니다.", HttpStatus.OK);
	}
}
