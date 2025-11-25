package com.etfinder.mvc.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.etfinder.mvc.model.dto.EtfProduct;

@Mapper
public interface EtfMapper {

    // XML의 <insert id="insertEtf"> 와 연결됨
    void insertEtf(EtfProduct etf);

    // etf_code로 찾아 current_price만 갱신
    // 매개변수 고민 필요 -> 이름 변경
    void updateCurrentPrice(@Param("etfCode") String etfCode,
                            @Param("currentPrice") Integer currentPrice);
}
