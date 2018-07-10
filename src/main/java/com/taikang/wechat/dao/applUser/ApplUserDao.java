package com.taikang.wechat.dao.applUser;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhenghuan
 * @Date 2018/7/9 10:17
 * @Description  用户申请信息接口层
 * @Version 1.1
 */
@Mapper
public interface ApplUserDao {

    public String isExistEmaile(String userEmail);
    public String isExistPhone(String userPhone);
}
