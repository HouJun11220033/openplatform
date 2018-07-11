package com.taikang.wechat.service.wechatthiredservice.impl;


import com.alibaba.fastjson.JSON;
import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.dao.verifyTicketDao.VerifyTicketDao;
import com.taikang.wechat.model.weChat.BigInfo;
import com.taikang.wechat.model.weChat.VerifyTicket;
import com.taikang.wechat.model.weChat.WeChatGetAuthorizationInfoVo;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import com.taikang.wechat.service.wechatthiredservice.WeChatThridService;
import com.taikang.wechat.utils.WeChatUtils;
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
public class WeChatThirdServiceImpl implements WeChatThridService {


    @Override
    public BigInfo getAuthAccessToken(String authorizationCode) {
        WeChatGetAuthorizationInfoVo
                .builder()
                .component_appid(WeChatContants.THRID_APPID)
                .authorization_code(authorizationCode)
                .build();

        String result = WeChatUtils.postUrl(WeChatContants.GET_AUTH_ACCESS_TOKEN, WeChatGetAuthorizationInfoVo
                .builder()
                .component_appid(WeChatContants.THRID_APPID)
                .authorization_code(authorizationCode)
                .build());
        return JSON.parseObject(result, BigInfo.class);
    }
}
