package com.taikang.wechat.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author: zqs
 * @description 数据访问层接口
 * @date: Created in 14:28 2018/7/5
 */
@Mapper
public interface ShowDao {
    /**
     * @author: 张清森
     * @description: 通过id 查询姓名
     * @date： 2018/7/5
     */
    String selectNameById(int id);
}
