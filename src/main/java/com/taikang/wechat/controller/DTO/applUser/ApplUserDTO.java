package com.taikang.wechat.controller.DTO.applUser;

import lombok.Data;

/**
 * @Author zhenghuan
 * @Date 2018/7/9 10:46
 * @Description 前段传过来的注册信息
 * @Version 1.0
 */
@Data
public class ApplUserDTO {

    //用户邮箱
    private String userEmail;

    //用户手机号
    private String userPhone;

    //用户密码
    private String password;

    //用户确认密码
    private String confirmPassword;

    //验证码
    private String identifyingCode;
}
