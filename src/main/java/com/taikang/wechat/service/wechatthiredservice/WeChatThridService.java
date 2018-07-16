package com.taikang.wechat.service.wechatthiredservice;

import com.taikang.wechat.model.weChat.AuthorizationInfo;
import com.taikang.wechat.model.weChat.AuthorizerInfo;
import com.taikang.wechat.model.weChat.BigAuthorizationInfo;
import com.taikang.wechat.model.weChat.BigInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    /**
     * 获取某一公众号下的粉丝总数量
     * @param bigAuthorizationInfo 公众号信息
     * @return 数量
     */
    Long getFensiTotal(BigAuthorizationInfo bigAuthorizationInfo);

    /**
     * 处理用户事件
     * @param request 请求
     * @param response 响应
     */
    void handleMessage(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
