package com.taikang.wechat.service;

import com.github.pagehelper.PageInfo;
import com.taikang.wechat.model.UserInfo;

/**
 * AUTHOR nicai
 * 方法说明：代码展示业务层接口
 * DATE Created in  2018/7/5
 */
public interface ShowService {
/**
 * @return  姓名
 * @param  id 主键id
 * @方法说明 通过ID查询姓名
 * @date 2018/7/5
 * @author 张清森
 */
    String selectByIdService(Integer id);

    PageInfo<UserInfo> select();
}
