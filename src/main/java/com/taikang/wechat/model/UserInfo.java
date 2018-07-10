package com.taikang.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    //主键ID
    private Integer id;
    //姓名
    private String userName;
    //密码
    private String passWord;
    //手机号
    private String mobile;
    //邮箱
    private String email;
    //状态 1：未登录 2：已登录
    private Integer state;

}
