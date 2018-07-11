package com.taikang.wechat.service.commponentVerifyTivket.impl;


import com.taikang.wechat.model.weChat.VerifyTicket;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerifyticketServiceImplTest {
    @Autowired
    private VerifyTicketService verifyTicketService;

    @Test
    @Rollback(false)
    public void insertTicket() throws Exception {
        VerifyTicket verifyTicket = new VerifyTicket();
        verifyTicket.setComponentVerifyTicket("dgsdfgdfherther2h554hr6h");
        verifyTicketService.insertTicket(verifyTicket);
    }

    @Test
    public void selectTicket() throws Exception {
        VerifyTicket verifyTicket = verifyTicketService.selectTicket();
        String verifyTicketId = verifyTicket.getVerifyTicketId();
        Long begTime = verifyTicket.getBegTime();
        System.out.println(verifyTicketId+begTime);
    }

    @Test
    public void deleteTicket() throws Exception {
        verifyTicketService.deleteTicket();
    }

}