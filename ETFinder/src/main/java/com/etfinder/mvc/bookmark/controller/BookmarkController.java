package com.etfinder.mvc.bookmark.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.bookmark.dto.Bookmark;
import com.etfinder.mvc.bookmark.service.BookmarkService;
import com.etfinder.mvc.user.dto.User;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

	private final BookmarkService bookmarkService;

	public BookmarkController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	/**
	 * 1. 북마크 추가 POST /api/bookmarks/{etfId}?userId=1
	 */
	@PostMapping("/{etfId}")
	public ResponseEntity<String> add(
	        @PathVariable Long etfId,
	        @AuthenticationPrincipal User user
	) {
	    if (user == null) {
	        return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
	    }

	    Long userId = user.getUserId();
	    int res = bookmarkService.addBookmark(userId, etfId);

	    if (res == 0)
	        return new ResponseEntity<>("이미 북마크된 ETF입니다.", HttpStatus.OK);

	    return new ResponseEntity<>("북마크가 추가되었습니다.", HttpStatus.OK);
	}


	/**
	 * 2. 북마크 해제 DELETE /api/bookmarks/{etfId}?userId=1
	 */
	@DeleteMapping("/{etfId}")
	public ResponseEntity<?> remove(
	        @PathVariable Long etfId,
	        @AuthenticationPrincipal User user
	){
	    if (user == null) return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

	    Long userId = user.getUserId();
	    int result = bookmarkService.removeBookmark(userId, etfId);

	    if (result > 0) return ResponseEntity.ok("북마크 해제됨");

	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


	/**
	 * 3. 북마크 목록 조회 GET /api/bookmarks?userId=1
	 */
	@GetMapping
	public ResponseEntity<?> list(@AuthenticationPrincipal User user) {

		if (user == null) return new ResponseEntity<>("로그인 필요", HttpStatus.UNAUTHORIZED);

	    List<Bookmark> list = bookmarkService.getBookmarksByUserId(user.getUserId());

		if (list == null || list.size() == 0) // 오류 발생
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		// 정상 조회
		return new ResponseEntity<List<Bookmark>>(list, HttpStatus.OK);

	}

}
