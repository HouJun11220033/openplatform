package com.taikang.wechat.dao;

import com.taikang.wechat.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zqs
 * @description 数据访问层接口
 * @date: Created in 14:28 2018/7/5
 */
@Mapper
public interface UserInfoDao {
    /**
     * @author: 张清森
     * @description: 通过id 查询姓名
     * @date： 2018/7/5
     */
    String selectNameById(int id);

    List<UserInfo> select();
}
