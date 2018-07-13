package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRefreshTokenVo implements Serializable {
    private static final long serialVersionUID = 6482554200594374330L;
    /**
     * 第三方id
     */
    private String component_appid;
    /**
     * 授权方id
     */
    private String authorizer_appid;
    /**
     * 授权方刷新令牌
     */
    private String authorizer_refresh_token;
}
