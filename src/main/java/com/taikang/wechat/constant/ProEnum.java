package com.taikang.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
public enum ProEnum {

    YES_NO_0(0, "否"),
    YES_NO_1(1, "是"),
    ;

    @Getter
    private Integer code;
    @Setter
    private String message;

}