package com.taikang.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.isNull;

/**
 * 张清森：01000-02000
 */
@AllArgsConstructor
public enum ExceptionEnum implements IExceptionEnum {
    //------------------------------系统类------------------------------
    //region 系统类
    INVOKING_ERROR("CPYY-00000", "操作失败"),
    INVOKING_SUCCESS("CPYY-00001", "调用成功"),
    SYS_ERROR("CPYY-00002", "系统异常"),
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