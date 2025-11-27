package com.etfinder.mvc.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.user.dto.UserResponse;
import com.etfinder.mvc.user.dto.UserUpdateRequest;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/me")
	public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal String providerId){
		UserResponse myInfo = userService.getUserInfo(providerId);
		if(myInfo != null) {
			return new ResponseEntity<UserResponse>(myInfo, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("회원 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/me")
	public ResponseEntity<String> updateMyInfo(@AuthenticationPrincipal String providerId, @RequestBody UserUpdateRequest request){
		int result = userService.updateUser(providerId, request);
		if(result > 0) {
			return new ResponseEntity<String>("회원 정보 업데이트가 완료되었습니다.", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("회원 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/me")
	public ResponseEntity<String> deleteUser(@AuthenticationPrincipal String providerId){
		int result = userService.deleteUser(providerId);
		if(result > 0) {
			return new ResponseEntity<String>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("회원 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
	}
}
