package com.taikang.wechat.constant;

import lombok.Getter;
import lombok.Setter;
import static java.util.Objects.isNull;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum HRSCExceptionEnum implements IExceptionEnum {
    //-----------------------------异常-----------------------------
    PARAMGRAM_MISS("TK-00002", "参数缺失"),
    ;
    @Getter
    private String code;
    @Setter
    private String message;

    @Override
    public String getMessage() {
        return isNull(this.message) ? null : this.message;
    }

}
