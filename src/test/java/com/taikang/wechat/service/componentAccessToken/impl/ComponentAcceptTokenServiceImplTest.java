package com.taikang.wechat.service.componentAccessToken.impl;

import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComponentAcceptTokenServiceImplTest {
    @Autowired
    private ComponentAcceptTokenService acceptTokenService;
    @Test
    @Rollback(false)
    public void insertAcceptToken() throws Exception {
        ComponentAcceptToken componentAcceptToken = new ComponentAcceptToken();
        componentAcceptToken.setExpiresIn(72L);
        componentAcceptToken.setAcceptToken("sdfsdgsdg45sdg45s6dfgsg");
        acceptTokenService.insertAcceptToken(componentAcceptToken);
    }

    @Test
    public void selectAcceptToken() throws Exception {
        ComponentAcceptToken componentAcceptToken = acceptTokenService.selectAcceptToken();
    }

    @Test
    public void deleteAcceptTokent() throws Exception {
        acceptTokenService.deleteAcceptTokent();
    }

}