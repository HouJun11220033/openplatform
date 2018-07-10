package com.taikang.wechat.service.commponentVerifyTivket.impl;


import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.VerifyTicket;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialException;
import java.util.Date;


/**
 * AUTHOR 张清森
 * 方法说明：微信凭证业务层接口
 * DATE Created in  2018/7/10
 */
@Service
@Slf4j
public class VerifyticketServiceImpl implements VerifyTicketService {

    /**
     * 新增凭证数据
     * @param verifyTicket 凭证
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void insertTicket(VerifyTicket verifyTicket) {
        if (verifyTicket==null||verifyTicket.getComponentVerifyTicket()==null){
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        verifyTicket.setBegTime(System.currentTimeMillis());
        verifyTicket.setExpiresIn(7200000L);
        verifyTicket.setUpdateTime(new Date());
        verifyTicket.setCreateTime(new Date());

    }

    /**
     * 查询凭证
     * @return 凭证数据
     */
    @Override
    public ComponentAcceptToken selectTicket() {

        return null;
    }

    /**
     * 删除凭证
     */
    @Override
    public void deleteTicket() {

    }
}
