package com.etfinder.mvc.bookmark.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.bookmark.dto.Bookmark;
import com.etfinder.mvc.bookmark.service.BookmarkService;

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
	public ResponseEntity<String> add(@PathVariable("etfId") Long etfId, @RequestParam("userId") Long userId) {

		try {
			int res = bookmarkService.addBookmark(userId, etfId);

			// 정상적으로 서비스가 실행되었고, 0이면 "이미 존재"
			if (res == 0)
				return new ResponseEntity<>("이미 북마크된 ETF입니다.", HttpStatus.OK);

			return new ResponseEntity<>("북마크가 추가되었습니다.", HttpStatus.OK);

		} catch (Exception e) {
			// 오류 상황
			return new ResponseEntity<>("서버 오류로 북마크 추가에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * 2. 북마크 해제 DELETE /api/bookmarks/{etfId}?userId=1
	 */
	@DeleteMapping("/{etfId}")
	public ResponseEntity<?> remove(@PathVariable("etfId") Long etfId, @RequestParam("userId") Long userId) {

		int result = bookmarkService.removeBookmark(userId, etfId);

		if (result > 0) // 정상 해제
			return new ResponseEntity<String>("북마크가 정상적으로 해제되었습니다", HttpStatus.OK);

		// 오류 발생
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

	}

	/**
	 * 3. 북마크 목록 조회 GET /api/bookmarks?userId=1
	 */
	@GetMapping
	public ResponseEntity<?> list(@RequestParam("userId") Long userId) {

		List<Bookmark> list = bookmarkService.getBookmarksByUserId(userId);

		if (list == null || list.size() == 0) // 오류 발생
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		// 정상 조회
		return new ResponseEntity<List<Bookmark>>(list, HttpStatus.OK);

	}

}
