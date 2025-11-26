package com.etfinder.mvc.bookmark.service;

import java.util.List;

import com.etfinder.mvc.bookmark.dto.Bookmark;

public interface BookmarkService {
	
	// 1. 북마크 추가
	int addBookmark(Long userId, Long etfId);
	
	// 2. 북마크 해제
	int removeBookmark(Long userId, Long etfId);
	
	// 3. 북마크 조회
	List<Bookmark> getBookmarksByUserId(Long userId);
	
	// 4. 북마크 여부 확인 (중복 방지)
	boolean isBookmarked(Long userId, Long etfId);
}
