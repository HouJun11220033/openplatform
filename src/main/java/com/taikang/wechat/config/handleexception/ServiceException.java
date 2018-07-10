package com.taikang.wechat.config.handleexception;


import com.taikang.wechat.constant.IExceptionEnum;

/**
 * 创建时间：2018/一月/13
 *
 * @author 张清森
 * 类名：ServiceException
 * 描述：Service层异常
 */
public class ServiceException extends EnumException {
    private static final long serialVersionUID = 1L;

    /**
     * 包含自定义以及枚举异常
     *
     * @param responseEnum 异常枚举
     * @param suffix       自定义
     */
    public ServiceException(IExceptionEnum responseEnum, String suffix) {
        super(responseEnum, suffix);
    }

    /**
     * 只包含异常枚举
     *
     * @param responseEnum 异常枚举
     */
    public ServiceException(IExceptionEnum responseEnum) {
        super(responseEnum);
    }
}