package com.taikang.wechat.service.applUserService;

import com.taikang.wechat.controller.VO.ApplUser.ApplUserVO;

/**
 * @Author zhenghuan
 * @Date 2018/7/9 10:52
 * @Description  用户申请信息业务层接口
 * @Version 1.0
 */
public interface ApplUserService {

    /**
     * @Author zhenghuan
     * @Date 2018/7/9 10:55
     * @Description  用户注册校验邮箱
     * @Version 1.0
     * @param userEmail 前段传入的信息
     */
     ApplUserVO isUserEmail(String userEmail);


}
