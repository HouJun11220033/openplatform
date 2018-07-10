package com.taikang.wechat.model.weChat;

import lombok.Data;

import java.io.Serializable;
@Data
public class WeChatPreAuthCodeVo implements Serializable {
    private static final long serialVersionUID = -2411219201610744698L;
    private String pre_auth_code;
    private Long expires_in;
}
