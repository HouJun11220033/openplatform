package com.taikang.wechat.utils.voutils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * webservice返回客户端对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -8688072985562369039L;

    /**
     * 响应状态码
     */
    private String code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;
}