package com.taikang.wechat.service.wechatthiredservice.impl;


import com.alibaba.fastjson.JSON;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.*;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import com.taikang.wechat.service.wechatthiredservice.WeChatThridService;
import com.taikang.wechat.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * AUTHOR 张清森
 * 方法说明：微信凭证业务层接口
 * DATE Created in  2018/7/10
 */
@Service
@Slf4j
public class WeChatThirdServiceImpl implements WeChatThridService {
    private final ComponentAcceptTokenService componentAcceptTokenService;

    @Autowired
    public WeChatThirdServiceImpl(ComponentAcceptTokenService componentAcceptTokenService) {
        this.componentAcceptTokenService = componentAcceptTokenService;
    }

    /**
     * 获取授权码
     *
     * @param authorizationCode 授权码
     * @return 大对象
     */
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

    /**
     * 刷新 authorizer_access_token
     *
     * @param authorizationInfo 授权对象
     * @return 授权对象
     */
    @Override
    public AuthorizerInfo getAuthorizationInfo(AuthorizationInfo authorizationInfo) {
        //查询commtoken
        ComponentAcceptToken componentAcceptToken = componentAcceptTokenService.selectAcceptToken();
        String acceptToken = componentAcceptToken.getAcceptToken();
        WeChatGetAuthorInfo weChatGetAuthorInfo = new WeChatGetAuthorInfo();
        weChatGetAuthorInfo.setAuthorizer_appid(WeChatContants.THRID_APPID);
        weChatGetAuthorInfo.setComponent_appid(authorizationInfo.getAuthorizer_appid());
        //调用微信接口获取全部授权信息
        String format = String.format(WeChatContants.GET_AUTHOR_INFO, acceptToken);
        String authorInfo = WeChatUtils.postUrl(format, weChatGetAuthorInfo);
        BigInfo bigInfo = JSON.parseObject(authorInfo, BigInfo.class);
        AuthorizerInfo authorizerInfo = bigInfo.getAuthorizer_info();
        return authorizerInfo;
    }
}
