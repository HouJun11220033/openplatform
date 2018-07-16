package com.taikang.wechat.service.componentAccessToken.impl;


import com.taikang.wechat.dao.componentaccepttoken.ComponentAcceptTokenDao;
import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


/**
 * AUTHOR 张清森
 * 方法说明：微信令牌业务层接口
 * DATE Created in  2018/7/10
 */
@Service
@Slf4j
public class ComponentAcceptTokenServiceImpl implements ComponentAcceptTokenService {

    private final ComponentAcceptTokenDao componentAcceptTokenDao;

    @Autowired
    public ComponentAcceptTokenServiceImpl(ComponentAcceptTokenDao componentAcceptTokenDao) {
        this.componentAcceptTokenDao = componentAcceptTokenDao;
    }

    /**
     * 新增凭证数据
     * COMPONENT_ACCEPT_TOKEN
     *
     * @param componentAcceptToken 令牌
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertAcceptToken(ComponentAcceptToken componentAcceptToken) {
        if (componentAcceptToken != null && componentAcceptToken.getAcceptToken() != null) {
            componentAcceptToken.setComponentAcceptTokenId(UUID.randomUUID().toString());
            componentAcceptToken.setBegTime(System.currentTimeMillis()/1000L);
            componentAcceptToken.setCreateTime(new Date());
            componentAcceptToken.setUpdateTime(new Date());
            this.deleteAcceptTokent();
            componentAcceptTokenDao.insertAcceptTokenDao(componentAcceptToken);
        }
    }

    /**
     * 查询令牌数据
     * COMPONENT_ACCEPT_TOKEN
     */
    @Override
    public ComponentAcceptToken selectAcceptToken() {
        return componentAcceptTokenDao.selectAcceptToken();
    }

    /**
     * 删除令牌数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAcceptTokent() {
        ComponentAcceptToken componentAcceptToken = this.selectAcceptToken();
        if (componentAcceptToken != null) {
            componentAcceptTokenDao.deleteAcceptToken(componentAcceptToken.getComponentAcceptTokenId());
        }
    }
}
