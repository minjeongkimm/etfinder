package com.etfinder.mvc.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.comment.dto.Comment;
import com.etfinder.mvc.comment.dto.CommentListResponse;
import com.etfinder.mvc.comment.service.CommentService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/etfs/{etfId}/comments")
public class CommentController {

	private final CommentService commentService;
	private final UserService userService;

	public CommentController(CommentService commentService, UserService userService) {
		this.commentService = commentService;
		this.userService = userService;
	}

	// 1. 한줄평 등록
	@PostMapping
	public ResponseEntity<String> add(@PathVariable("etfId") Long etfId, 
			@AuthenticationPrincipal String providerId,
			@RequestBody Comment request) {

		// 1) 로그인 여부 확인
		if (providerId == null)
			return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

		// 2) providerId로 실제 userId 찾기
		User user = userService.getUserByProviderId(providerId);
		if (user == null)
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);

		// 3) 클라이언트가 userId, etfId 조작 못하도록 서버에서 강제로 세팅
		Comment comment = new Comment();
		comment.setEtfId(etfId);
		comment.setUserId(user.getUserId());
		comment.setContent(request.getContent());

		// 4) 한줄평 등록 시도
		int res = commentService.addComment(comment);

		if (res == 0)
			return new ResponseEntity<>("한줄평 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity<>("한줄평 등록이 완료되었습니다.", HttpStatus.CREATED);

	}

	// 2. 한줄평 조회
	@GetMapping
	public ResponseEntity<CommentListResponse> list(@PathVariable("etfId") Long etfId) {

	    CommentListResponse result = commentService.getCommentsByEtfId(etfId);

	    return new ResponseEntity<CommentListResponse>(result, HttpStatus.OK);
	}

	// 3. 한줄평 수정
	@PutMapping("/{commentId}")
	public ResponseEntity<String> update(@PathVariable("etfId") Long etfId, 
			@PathVariable("commentId") Long commentId,
			@AuthenticationPrincipal String providerId, @RequestBody Comment request) {

		// 1) 로그인 여부 확인
		if (providerId == null)
			return new ResponseEntity<>("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);

		// 2) providerId로 실제 userId 찾기
		User user = userService.getUserByProviderId(providerId);
		if (user == null)
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);

		// 3) 댓글 존재 여부, 작성자 여부
		int res = commentService.updateComment(commentId, user.getUserId(), request.getContent());
		if (res == 0)
			return new ResponseEntity<>("수정 권한이 없거나 댓글이 존재하지 않습니다.", HttpStatus.FORBIDDEN);

		return new ResponseEntity<>("한줄평이 수정되었습니다.", HttpStatus.OK);

	}

	// 4. 한줄평 삭제
	@DeleteMapping("/{commentId}")
	public ResponseEntity<String> delete(@PathVariable("etfId") Long etfId, @PathVariable("commentId") Long commentId,
			@AuthenticationPrincipal String providerId) {

		// 1) 로그인 여부 확인
		if (providerId == null)
			return new ResponseEntity<>("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);

		// 2) providerId로 실제 userId 찾기
		User user = userService.getUserByProviderId(providerId);
		if (user == null)
			return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);

		// 3) 댓글 존재 여부, 작성자 여부
		int res = commentService.deleteComment(commentId, user.getUserId());
		
		if(res==0)
			return new ResponseEntity<>("삭제 권한이 없거나 댓글이 존재하지 않습니다.", HttpStatus.FORBIDDEN);
		
		return new ResponseEntity<>("한줄평이 삭제되었습니다.", HttpStatus.OK);
		
	}

}
