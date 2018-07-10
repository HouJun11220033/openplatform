package com.taikang.wechat.controller.VO.ApplUser;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author zhenghuan
 * @Date 2018/7/9 14:01
 * @Description 注册校验返回给前端的信息
 * @Version 1.0
 */
@Data
public class ApplUserVO implements Serializable {

    private  String id;
    private  String message;
}
