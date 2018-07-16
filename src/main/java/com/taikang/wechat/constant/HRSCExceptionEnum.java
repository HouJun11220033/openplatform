package com.taikang.wechat.constant;

import lombok.Getter;
import lombok.Setter;
import static java.util.Objects.isNull;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum HRSCExceptionEnum implements IExceptionEnum {
    //-----------------------------异常-----------------------------
    PARAMGRAM_MISS("TK-00002", "参数缺失!"),
    PRE_AUTH_CODE_MISS("TK-00003", "预授权码为空!"),
    USER_ID_MISS("TK-00004", "账户id为空!"),
    UNABLE_GET_AUTHOR_INFO_BY_ID("TK-00005", "未找到相应授权信息!"),
    CONFIG_INFO_ERROR("TK-00006", "平台配置参数有误!"),
    COMPONENT_ACCESS_TOKEN_MISS("TK-00007", "预通行令牌丢失!"),
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
