package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatGetAuthorInfo implements Serializable {
    private static final long serialVersionUID = -5673086234192743689L;
    private String component_appid ;
    private String authorizer_appid ;
}
