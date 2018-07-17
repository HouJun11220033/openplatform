package com.taikang.wechat.service.wechatthiredservice.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatKeFuSendTextMessageVo implements Serializable {
    private static final long serialVersionUID = 2990037752640965565L;
    private String msgtype;
    private String touser;
    private WeChatKeFuSendTextVo text;
}
