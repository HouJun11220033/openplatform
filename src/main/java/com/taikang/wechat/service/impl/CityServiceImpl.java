package com.taikang.wechat.service.impl;

import com.taikang.wechat.dao.CityDao;
import com.taikang.wechat.model.CityModel;
import com.taikang.wechat.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dongxie on 2018/7/7.
 */
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityDao cityDao;

    @Override
    public void insert(CityModel city) {
        CityModel city1 = new CityModel();
        city1.setCountry("china");
        city1.setName("中国");
        city1.setState("Beijing");
        cityDao.insertCity(city1);
    }

    @Override
    public List<CityModel> selectAll() {
        return cityDao.selectAll();
    }
}
