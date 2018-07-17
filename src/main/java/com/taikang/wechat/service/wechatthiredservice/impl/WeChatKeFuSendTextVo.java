package com.taikang.wechat.service.wechatthiredservice.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatKeFuSendTextVo implements Serializable {
    private static final long serialVersionUID = 1330801536005589006L;
    private String content;
}
