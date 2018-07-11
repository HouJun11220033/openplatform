package com.taikang.wechat.service.wechatthiredservice;

import com.taikang.wechat.model.weChat.BigInfo;

/**
 * 微信开放平台
 * 张清森
 */
public interface WeChatThridService {

    /**
     * 获取
     * @param authorizationCode 授权码
     * @return 大对象
     */
    BigInfo getAuthAccessToken(String authorizationCode);
}
