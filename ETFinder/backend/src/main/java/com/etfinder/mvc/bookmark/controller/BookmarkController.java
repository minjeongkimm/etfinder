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
import org.springframework.web.bind.annotation.RestController;

import com.etfinder.mvc.bookmark.dto.Bookmark;
import com.etfinder.mvc.bookmark.service.BookmarkService;
import com.etfinder.mvc.user.dto.User;
import com.etfinder.mvc.user.service.UserService;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;

    public BookmarkController(BookmarkService bookmarkService, UserService userService) {
        this.bookmarkService = bookmarkService;
        this.userService = userService;
    }

    /**
     * 1. 북마크 추가 POST /api/bookmarks/{etfId}
     */
    @PostMapping("/{etfId}")
    public ResponseEntity<String> add(
            @PathVariable Long etfId,
            @AuthenticationPrincipal String providerId
    ) {
        // 1) 로그인 여부 확인
        if (providerId == null) {
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
        }

        // 2) providerId → userId 조회
        User user = userService.getUserByProviderId(providerId);
        if (user == null) {
            return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
        }

        // 3) 북마크 추가
        int res = bookmarkService.addBookmark(user.getUserId(), etfId);

        if (res == 0)
            return new ResponseEntity<>("이미 북마크된 ETF입니다.", HttpStatus.CONFLICT);

        return new ResponseEntity<>("북마크가 추가되었습니다.", HttpStatus.CREATED);
    }


    /**
     * 2. 북마크 해제 DELETE /api/bookmarks/{etfId}
     */
    @DeleteMapping("/{etfId}")
    public ResponseEntity<?> remove(
            @PathVariable Long etfId,
            @AuthenticationPrincipal String providerId
    ) {
        // 1) 로그인 여부 확인
        if (providerId == null)
            return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

        // 2) providerId → userId 조회
        User user = userService.getUserByProviderId(providerId);
        if (user == null)
            return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);

        // 3) 북마크 해제 실행
        int result = bookmarkService.removeBookmark(user.getUserId(), etfId);

        if (result > 0)
            return ResponseEntity.ok("북마크 해제되었습니다.");

        return new ResponseEntity<>("북마크가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
    }


    /**
     * 3. 북마크 목록 조회 GET /api/bookmarks
     */
    @GetMapping
    public ResponseEntity<?> list(@AuthenticationPrincipal String providerId) {

        // 1) 로그인 여부 확인
        if (providerId == null)
            return new ResponseEntity<>("로그인 필요", HttpStatus.UNAUTHORIZED);

        // 2) providerId → userId 조회
        User user = userService.getUserByProviderId(providerId);
        if (user == null)
            return new ResponseEntity<>("유저 정보를 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);

        List<Bookmark> list = bookmarkService.getBookmarksByUserId(user.getUserId());

        if (list == null || list.isEmpty()) 
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
