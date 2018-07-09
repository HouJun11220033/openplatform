package com.taikang.wechat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CityModel {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String state;

    private String country;
}
