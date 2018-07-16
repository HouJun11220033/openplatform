package com.taikang.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
public enum ContantsEnum{
    //------------------------------微信对接------------------------------
    ENVEBT_TYPE_1("subscribe", "粉丝关注"),
    ENVEBT_TYPE_2("unsubscribe", "取消关注"),;

    @Getter
    private String code;
    @Setter
    private String message;

}