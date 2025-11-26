package com.etfinder.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etfinder.mvc.model.dto.Bookmark;
import com.etfinder.mvc.model.mapper.BookmarkMapper;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	
	@Autowired
	private BookmarkMapper bookmarkMapper;
	
	// 1. 북마크 추가
	@Override
	public int addBookmark(Long userId, Long etfId) {
		// 이미 북마크 되어 있으면 0 리턴
		if(bookmarkMapper.isBookmarked(userId, etfId))
			return 0;
		// 아니면 새로 추가 (성공 시 1 반환)
		return bookmarkMapper.addBookmark(userId, etfId);
	}

	// 2. 북마크 해제
	@Override
	public int removeBookmark(Long userId, Long etfId) {
		return bookmarkMapper.removeBookmark(userId, etfId);
	}

	// 3. 북마크 조회
	@Override
	public List<Bookmark> getBookmarksByUserId(Long userId) {
		return bookmarkMapper.getBookmarksByUserId(userId);
	}

	// 4. 북마크 여부 확인 (중복 방지)
	@Override
	public boolean isBookmarked(Long userId, Long etfId) {
		return bookmarkMapper.isBookmarked(userId, etfId);
	}

}
