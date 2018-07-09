package com.taikang.wechat.dao;

import com.taikang.wechat.model.CityModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityDao {
    /**
     * 增加
     */
    void insertCity(CityModel city);

    /**
     * 查询全部列表
     */
    List<CityModel> selectAll();
}
