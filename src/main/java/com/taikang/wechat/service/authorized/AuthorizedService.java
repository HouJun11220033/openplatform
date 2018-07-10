package com.taikang.wechat.service.authorized;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信授权
 * @author 张清森
 * @createTime 2018.7.9
 */
public interface AuthorizedService {
    /**
     * 处理微信授权事件
     * @param request 请求
     * @param response 响应
     * @throws Exception
     */
    void handleAuthorize(HttpServletRequest request,
                         HttpServletResponse response) throws Exception;
}
