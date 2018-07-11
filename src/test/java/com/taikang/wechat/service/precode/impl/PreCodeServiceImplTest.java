package com.taikang.wechat.service.precode.impl;

import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.PreCode;
import com.taikang.wechat.service.precode.PreCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PreCodeServiceImplTest {
    @Autowired
    private PreCodeService preCodeService;

    @Test
    @Rollback(false)
    public void insertPreCode() throws Exception {
        PreCode preCode = new PreCode();
        preCode.setExpiresIn(600L);
        preCode.setPreCode("gsdfgsd54fgs6d5g4s");
        preCodeService.insertPreCode(preCode);
    }

    @Test
    public void selectPreCode() throws Exception {
        PreCode preCode = preCodeService.selectPreCode();
        System.out.println(preCode.getPreCodeId());
    }

    @Test
    public void deletePreCode() throws Exception {
        preCodeService.deletePreCode();
    }

    @Test
    @Rollback(false)
    public void updatePreCodeByPreCodeId() throws Exception {
        PreCode preCode = preCodeService.selectPreCode();
        if (preCode != null) {
            preCodeService.updatePreCodeByPreCodeId(preCode);
        }
    }

    @Test
    public void selectAndUpdata() throws Exception {
        PreCode preCode = preCodeService.selectPreCode();
        if (preCode != null) {
            preCodeService.selectAndUpdata(preCode);
        }
    }

}