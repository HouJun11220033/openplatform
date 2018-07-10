package com.taikang.wechat.config.handleexception;


import com.taikang.wechat.constant.IExceptionEnum;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.isNull;


/**
 * Created by 张清森 on 2017/6/13.
 */
@Setter
class EnumException extends RuntimeException {
    private static final long serialVersionUID = -3456483498862876456L;

    /**
     * 错误的枚举返回
     */
    @Getter
    private IExceptionEnum responseEnum;

    /**
     * 补充错误信息
     */
    private String suffix;

    EnumException(IExceptionEnum responseEnum) {
        this.responseEnum = responseEnum;
    }

    EnumException(IExceptionEnum responseEnum, String suffix) {
        super();
        this.responseEnum = responseEnum;
        this.suffix = suffix;
    }

    String getSuffix() {
        return isNull(this.suffix) ? null : this.suffix;
    }
}