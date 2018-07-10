package com.taikang.wechat.model.applUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhenghuan
 * @Date 2018/7/9 10:07
 * @Description 用户申请信息
 * @Version 1.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplUser {

    //用户ID
    private String userId;

    //用户手机号
    private String userPhone;

    //用户邮箱
    private String userEmail;

    //用户密码
    private String password;

    //用户是否授权
    private String accreditFlag;

    //注册时间
    private String registTime;

    //修改时间
    private String updateTime;

}
