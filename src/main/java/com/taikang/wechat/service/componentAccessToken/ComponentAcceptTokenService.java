package com.taikang.wechat.service.componentAccessToken;


import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.VerifyTicket;

/**
 * AUTHOR 张清森
 * 方法说明：微信令牌业务层接口
 * DATE Created in  2018/7/10
 */
public interface ComponentAcceptTokenService {
    /**
     * 新增令牌数据
     * @param componentAcceptToken 令牌
     */
    void insertAcceptToken(ComponentAcceptToken componentAcceptToken);

    /**
     * 查询令牌
     * @return 令牌数据
     */
    ComponentAcceptToken selectAcceptToken();

    /**
     * 删除令牌
     */
    void deleteAcceptTokent();
}
