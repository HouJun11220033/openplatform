package com.taikang.wechat.controller.authorize;

import com.alibaba.fastjson.JSON;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.WeChatThridGetTokenVo;
import com.taikang.wechat.service.authorized.AuthorizedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 微信授权
 *
 * @author 张清森
 * @createTime 2018.7.9
 */
@RestController
@RequestMapping("weChat")
@Slf4j
public class AuthorizeController {
    private final AuthorizedService authorizedService;

    @Autowired
    public AuthorizeController(AuthorizedService authorizedService) {
        this.authorizedService = authorizedService;
    }

    /**
     * 微信授权事件接受
     *
     * @param response 响应
     * @param request  请求
     */
    @RequestMapping(value = "/authorize", method = {RequestMethod.GET, RequestMethod.POST})
    public void acceptAuthorizeEvent(
            HttpServletResponse response, HttpServletRequest request) {
        try {
            //处理授权事件
            authorizedService.handleAuthorize(request, response);
            PrintWriter pw = response.getWriter();
            pw.write("success");
            pw.flush();
        } catch (Exception e) {
            log.info("错误信息{}", e.getMessage());
        }
    }
}
