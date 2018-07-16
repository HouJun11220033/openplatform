package com.taikang.wechat.service.precode.impl;


import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.dao.precode.PreCodeDao;
import com.taikang.wechat.model.weChat.*;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import com.taikang.wechat.service.precode.PreCodeService;
import com.taikang.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * AUTHOR 张清森
 * 方法说明：微信预授权码业务层接口
 * DATE Created in  2018/7/11
 */
@Service
@Slf4j
public class PreCodeServiceImpl implements PreCodeService {

    private final PreCodeDao preCodeDao;
    private final ComponentAcceptTokenService componentAcceptTokenService;
    private final VerifyTicketService verifyTicketService;

    @Autowired
    public PreCodeServiceImpl(PreCodeDao preCodeDao, ComponentAcceptTokenService componentAcceptTokenService, VerifyTicketService verifyTicketService) {
        this.preCodeDao = preCodeDao;
        this.componentAcceptTokenService = componentAcceptTokenService;
        this.verifyTicketService = verifyTicketService;
    }

    /**
     * 新增预授权码数据
     *
     * @param preCode 预授权码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertPreCode(PreCode preCode) {
        preCode.setBegTime(System.currentTimeMillis());
        preCode.setCreateTime(new Date());
        preCode.setPreCodeId("pre_code");
        preCode.setUpdateTime(new Date());
        preCodeDao.insertPreCodeDao(preCode);
    }

    /**
     * 查询预授权码
     *
     * @return 预授权码数据
     */
    @Override
    public PreCode selectPreCode() {

        PreCode preCode = preCodeDao.selectProCodeDao();
        //判断是否即将过期
        Long begTime = preCode.getBegTime();
        Long expiresIn = preCode.getExpiresIn();
        Long current = System.currentTimeMillis() / 1000L;
        //判断预授权码是否过期 过期
        if (begTime.compareTo(current - expiresIn) <= 0) {
            //查询令牌
            ComponentAcceptToken componentAcceptToken = componentAcceptTokenService.selectAcceptToken();
            //判断令牌是否存在 存在直接用 不存在被动刷新
            if (componentAcceptToken==null){
                //查询最新的凭证
                VerifyTicket verifyTicket = verifyTicketService.selectTicketNew();
                //如凭证不存在 抛异常
                if (verifyTicket==null){
                    throw new ServiceException(HRSCExceptionEnum.COMPONENT_ACCESS_TOKEN_MISS);
                }
                //存在则被动刷新令牌
                WeChatComponentAccessTokenVo componentAccessTokenVo = WeChatUtils.getComponentAccessToken(verifyTicket.getComponentVerifyTicket());
                ComponentAcceptToken acceptToken = new ComponentAcceptToken();
                acceptToken.setAcceptToken(componentAccessTokenVo.getComponent_access_token());
                acceptToken.setExpiresIn(componentAccessTokenVo.getExpires_in());
                componentAcceptTokenService.insertAcceptToken(acceptToken);
            }
            ComponentAcceptToken componentAcceptToken1 = componentAcceptTokenService.selectAcceptToken();
            //刷新预授权码
            Object[] object = {componentAcceptToken1.getAcceptToken()};
            //调用微信
            WeChatPreAuthCodeVo preAuthCodeVo = WeChatUtils.getWeChatPreAuthCodeVo(object);
            preCode.setPreCode(preAuthCodeVo.getPre_auth_code());
            preCode.setExpiresIn(preAuthCodeVo.getExpires_in());
            //将预授权码存在数据库中
            this.updatePreCodeByPreCodeId(preCode);
        }
        return preCode;
    }

    /**
     * 删除预授权码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePreCode() {

    }

    /**
     * 更新预授权码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreCodeByPreCodeId(PreCode preCode) {
        if (preCode == null || preCode.getPreCodeId() == null) {
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        preCode.setUpdateTime(new Date());
        preCode.setBegTime(System.currentTimeMillis()/1000L);
        preCodeDao.updatePreCodeByPreCodeId(preCode);
    }

    /**
     * 先查后改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAndUpdata(PreCode preCode) {
        PreCode selectPreCode = this.selectPreCode();
        if (selectPreCode != null) {
            this.updatePreCodeByPreCodeId(preCode);
        } else {
            this.insertPreCode(preCode);
        }
    }
}
