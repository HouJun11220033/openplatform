package com.taikang.wechat.service.wechatthiredservice;

import com.taikang.wechat.model.weChat.AuthorizationInfo;
import com.taikang.wechat.model.weChat.AuthorizerInfo;
import com.taikang.wechat.model.weChat.BigInfo;

/**
 * 微信开放平台
 * 张清森
 */
public interface WeChatThridService {

    /**
     * 获取授权码
     * @param authorizationCode 授权码
     * @return 大对象
     */
    BigInfo getAuthAccessToken(String authorizationCode);
    /**
     * 刷新 authorizer_access_token
     * @param authorizationInfo 授权对象
     * @return 授权对象
     */
    AuthorizerInfo getAuthorizationInfo(AuthorizationInfo authorizationInfo);
}
