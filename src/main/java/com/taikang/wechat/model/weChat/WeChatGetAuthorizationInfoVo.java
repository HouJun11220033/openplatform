package com.taikang.wechat.model.weChat;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class WeChatGetAuthorizationInfoVo implements Serializable {
    private static final long serialVersionUID = 1661708756956717347L;
    /**
     * 第三方平台appid
     */
    private String component_appid;

    /**
     * 授权code
     */
    private String authorization_code;
}
