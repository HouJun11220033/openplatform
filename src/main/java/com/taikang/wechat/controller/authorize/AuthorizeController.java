package com.taikang.wechat.controller.authorize;


import com.taikang.wechat.service.authorized.AuthorizedService;
import com.taikang.wechat.service.wechatthiredservice.WeChatThridService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 微信授权
 * @author 张清森
 */
@RestController
@RequestMapping("weChat")
@Slf4j
public class AuthorizeController {
    private final AuthorizedService authorizedService;
    private final WeChatThridService weChatThridService;
    @Autowired
    public AuthorizeController(AuthorizedService authorizedService, WeChatThridService weChatThridService) {
        this.authorizedService = authorizedService;
        this.weChatThridService = weChatThridService;
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
    @RequestMapping(value="/{appid}/callback",method={RequestMethod.GET,RequestMethod.POST})
    public void callBackEvent(@PathVariable String appid,
                              HttpServletResponse response,HttpServletRequest request){
        try {
            log.info(appid+"进入callback+++++++++++++++++++++++++++++++++");
            weChatThridService.handleMessage(request,response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
