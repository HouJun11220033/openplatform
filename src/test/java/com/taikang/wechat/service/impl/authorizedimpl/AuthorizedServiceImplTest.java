package com.taikang.wechat.service.impl.authorizedimpl;

import com.taikang.wechat.dao.authorDao.AuthorDao;
import com.taikang.wechat.model.weChat.BigAuthorizationInfo;
import com.taikang.wechat.service.authorized.AuthorizedService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizedServiceImplTest {
    @Autowired
    private AuthorizedService authorizedService;
    @Autowired
    private AuthorDao authorDao;
    @Test
    public void handleAuthorize() throws Exception {
    }

    @Test
    public void insertAuthorService() throws Exception {
    }

    @Test
    public void doRefreshToken() throws Exception {
    }

    @Test
    public void findBigAuthorizationInfoByid() throws Exception {
        BigAuthorizationInfo bigAuthorizationInfoByid = authorizedService.findBigAuthorizationInfoByid("a3906ae8-30fb-4708-9ee1-8d5eb701ea99");
        BigAuthorizationInfo authorizationInfoByIdDao = authorDao.findAuthorizationInfoByIdDao("a3906ae8-30fb-4708-9ee1-8d5eb701ea99");
        authorizedService.updateAuthorizationInfoById(bigAuthorizationInfoByid);
        System.out.println("");
    }

    @Test
    public void updateAuthorizationInfoById() throws Exception {

    }

}