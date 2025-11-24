package com.etfinder.mvc.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.etfinder.mvc.model.dto.EtfProduct;

@Mapper
public interface EtfMapper {

    // XML의 <insert id="insertEtf"> 와 연결됨
    void insertEtf(EtfProduct etf);

}
