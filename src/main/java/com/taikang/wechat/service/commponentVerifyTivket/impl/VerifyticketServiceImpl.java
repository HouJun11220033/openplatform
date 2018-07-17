package com.taikang.wechat.service.commponentVerifyTivket.impl;


import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.dao.verifyTicketDao.VerifyTicketDao;
import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.VerifyTicket;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


/**
 * AUTHOR 张清森
 * 方法说明：微信凭证业务层接口
 * DATE Created in  2018/7/10
 */
@Service
@Slf4j
public class VerifyticketServiceImpl implements VerifyTicketService {

    private final VerifyTicketDao verifyTicketDao;

    @Autowired
    public VerifyticketServiceImpl(VerifyTicketDao verifyTicketDao) {
        this.verifyTicketDao = verifyTicketDao;
    }

    /**
     * 新增凭证数据
     *
     * @param verifyTicket 凭证
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTicket(VerifyTicket verifyTicket) {
        if (verifyTicket == null || verifyTicket.getComponentVerifyTicket() == null) {
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        verifyTicket.setVerifyTicketId(UUID.randomUUID().toString());
        verifyTicket.setBegTime(System.currentTimeMillis());
        verifyTicket.setExpiresIn(7200L);
        verifyTicket.setUpdateTime(new Date());
        verifyTicket.setCreateTime(new Date());
        verifyTicketDao.insertTicketDao(verifyTicket);
        this.deleteTicket();
    }

    /**
     * 查询凭证
     *
     * @return 凭证数据
     */
    @Override
    public VerifyTicket selectTicket() {
        //获取最老的数据
        return verifyTicketDao.selectTicket();
    }

    /**
     * 删除凭证
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTicket() {
        //删除马上失效的数据
        long t = System.currentTimeMillis();
        VerifyTicket verifyTicket = this.selectTicket();
        if (verifyTicket != null) {
            Long begTime = verifyTicket.getBegTime();
            Long expiresIn = verifyTicket.getExpiresIn();
            Long middle = t-begTime;
            Long aLong=(middle-expiresIn);
            if (aLong.compareTo(new Long("1200"))<=0) {
                //删除第一条数据
                verifyTicketDao.deleteTicket(verifyTicket.getVerifyTicketId());
            }
        }
    }
    /**
     * 查询最新凭证
     * @return 凭证信息
     */
    @Override
    public VerifyTicket selectTicketNew() {
        return verifyTicketDao.selectTicketNewDao();
    }

}
