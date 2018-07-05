package com.taikang.wechat.controller.DTO;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 2695004671863926293L;
    //主键ID
    private Integer id;
    //姓名
    private String name;
}
