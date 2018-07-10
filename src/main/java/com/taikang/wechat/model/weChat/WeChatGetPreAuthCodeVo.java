package com.taikang.wechat.model.weChat;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
@Builder
public class WeChatGetPreAuthCodeVo implements Serializable {
    private static final long serialVersionUID = 7070892498522911495L;
    /**
     * 第三方平台id
     */
    private String component_appid ;
}
