package com.etfinder.mvc.bookmark.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.bookmark.dto.Bookmark;

@Mapper
public interface BookmarkMapper {

    // 1. 북마크 추가
    int addBookmark(@Param("userId") Long userId,
                    @Param("etfId") Long etfId);

    // 2. 북마크 해제
    int removeBookmark(@Param("userId") Long userId,
                       @Param("etfId") Long etfId);

    // 3. 유저별 북마크 조회
    List<Bookmark> getBookmarksByUserId(@Param("userId") Long userId);

    // 4. 이미 북마크 되어있는지 여부 확인
    boolean isBookmarked(@Param("userId") Long userId,
                         @Param("etfId") Long etfId);
}
