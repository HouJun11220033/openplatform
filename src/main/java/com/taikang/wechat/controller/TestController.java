package com.taikang.wechat.controller;


import com.taikang.wechat.model.CityModel;
import com.taikang.wechat.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private CityService cityService;

    @RequestMapping("/api/getAllCityList")
    public List<CityModel> getAllCityList() {
        return cityService.selectAll();
    }

    @RequestMapping("/api/insertCity")
    public void insertCity() {
        CityModel city = new CityModel();
        city.setState("China");
        city.setName("China");
        city.setCountry("Beijing");
        cityService.insert(city);
    }
}
