package com.taikang.wechat.constant;

/**
 * 异常枚举统一接口
 * 联系：张清森
 */
public interface IExceptionEnum {
    /**
     * @return 获取错误码
     */
    String getCode();

    /**
     * @return 获取错误信息
     */
    String getMessage();

    /**
     *
     * @param message 设置错误信息
     */
    void setMessage(String message);
}