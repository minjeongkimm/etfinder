package com.etfinder.mvc.like.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {

	 boolean existsLike(@Param("userId") Long userId,
             @Param("etfId") Long etfId);
}
