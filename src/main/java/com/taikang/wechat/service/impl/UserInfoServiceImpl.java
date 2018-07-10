package com.taikang.wechat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taikang.wechat.dao.UserInfoDao;
import com.taikang.wechat.model.UserInfo;
import com.taikang.wechat.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AUTHOR nicai
 * 方法说明：代码展示业务层接口
 * DATE Created in  2018/7/5
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoDao userInfoDao;
    @Autowired
    public UserInfoServiceImpl(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    /**
     * @return  姓名
     * @param  id 主键id
     * @方法说明 通过ID查询姓名
     * @date 2018/7/5
     * @author 张清森
     */
    @Override
    public String selectByIdService(Integer id) {
//        Page<Object> objects = PageHelper.startPage(0, 10)
        if (id==null) return null;
        return userInfoDao.selectNameById(id);
    }

    @Override
    public PageInfo<UserInfo> select() {
        PageHelper.startPage(0,5);
        List<UserInfo> select = userInfoDao.select();
        log.info("hello");
        return new PageInfo<>(select);
    }
}
