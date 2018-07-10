package com.taikang.wechat.model.weChat;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class WeChatThridGetTokenVo implements Serializable {
    private static final long serialVersionUID = 2982489120716310446L;
    //第三方id
    private String component_appid;
    //第三方mima
    private String component_appsecret;
    //凭证
    private String component_verify_ticket;

}
