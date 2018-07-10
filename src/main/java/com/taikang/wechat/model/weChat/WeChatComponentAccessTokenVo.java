package com.taikang.wechat.model.weChat;

import lombok.Data;

import java.io.Serializable;
@Data
public class WeChatComponentAccessTokenVo implements Serializable {
    private static final long serialVersionUID = 6553357717583915667L;
    /**
     * 第三方平台token
     */
    private String component_access_token;
    /**
     * 有效时间
     */
    private Long expires_in;
}
