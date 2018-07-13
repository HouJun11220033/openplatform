package com.taikang.wechat.service.authorized;

import com.taikang.wechat.model.weChat.BigAuthorizationInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizedServiceTest {
    @Autowired
    private AuthorizedService authorizedService;
    @Test
    public void handleAuthorize() throws Exception {
    }

    @Test
    @Rollback(false)
    public void insertAuthorService() throws Exception {
        BigAuthorizationInfo bigAuthorizationInfo = new BigAuthorizationInfo();
        bigAuthorizationInfo.setAlias("sdfgsd");
        bigAuthorizationInfo.setAuthorizer_appid("f4s6d5f4sd65f4sd6");
        bigAuthorizationInfo.setAuthorizer_access_token("f45sd6f45s6f45s6");
        bigAuthorizationInfo.setAuthorizer_refresh_token("f4s6d5f46s4");
        bigAuthorizationInfo.setExpires_in(7200L);
        bigAuthorizationInfo.setNick_name("fsf4");
        bigAuthorizationInfo.setHead_img("sd54f6s4f");
        bigAuthorizationInfo.setPrincipal_name("5f4s6d54fs6");
        bigAuthorizationInfo.setUser_name("fsd645f6s");
        bigAuthorizationInfo.setQrcode_url("45dfs6d54fs");
        bigAuthorizationInfo.setService_type(1);
        bigAuthorizationInfo.setVerify_type(1);
        bigAuthorizationInfo.setCreateTime(new Date());
        bigAuthorizationInfo.setBegTime(System.currentTimeMillis());
        bigAuthorizationInfo.setAuthorizationInfoId(UUID.randomUUID().toString());
        authorizedService.insertAuthorService(bigAuthorizationInfo);
    }


}