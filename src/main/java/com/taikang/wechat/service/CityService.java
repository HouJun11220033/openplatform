package com.taikang.wechat.service;

import com.taikang.wechat.model.CityModel;

import java.util.List;

/**
 * Created by dongxie on 2018/7/7.
 */

public interface CityService {

    /**
     * 增加
     */
    void insert(CityModel city);

    /**
     * 查询全部列表
     */
    List<CityModel> selectAll();
}
