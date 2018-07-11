package com.taikang.wechat.controller.author;


import com.taikang.wechat.constant.ExceptionEnum;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.BigInfo;
import com.taikang.wechat.model.weChat.PreCode;
import com.taikang.wechat.service.precode.PreCodeService;
import com.taikang.wechat.service.wechatthiredservice.WeChatThridService;
import com.taikang.wechat.utils.voutils.ResponseResult;
import com.taikang.wechat.utils.voutils.ResultResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 微信授权
 *
 * @author 张清森
 * @createTime 2018.7.11
 */
@RestController
@RequestMapping("author")
@Slf4j
public class AuthorController {
    private final PreCodeService preCodeService;
    private final WeChatThridService weChatThridService;

    @Autowired
    public AuthorController(PreCodeService preCodeService, WeChatThridService weChatThridService) {
        this.preCodeService = preCodeService;
        this.weChatThridService = weChatThridService;
    }

    /**
     * 授权页面
     */
    @GetMapping(value = "goAuthPage")
    public ResponseResult<Object> acceptAuthorizeEvent(HttpServletResponse response, HttpServletRequest request) {
        try {
            //查找预授权码
            PreCode preAuthCode = preCodeService.selectPreCode();
            if (preAuthCode == null) {
                return ResultResponseUtils.error(HRSCExceptionEnum.PRE_AUTH_CODE_MISS);
            }
            //********************跳转到授权页面********************************//
            Object[] args = {WeChatContants.THRID_APPID, preAuthCode, WeChatContants.OWN_SCOPE};
            String authorizationUrl = String.format(WeChatContants.THRID_AUTHORIZATION_CODE, args);
            log.info("跳转的URL：" + authorizationUrl);
            response.sendRedirect(authorizationUrl);
            return ResultResponseUtils.success();
        } catch (Exception e) {
            log.info("调用失败!");
            return ResultResponseUtils.error(ExceptionEnum.INVOKING_ERROR);
        }
    }

    /**
     * 处理微信授权事件完成跳转URL
     *
     * @return
     */
    @RequestMapping(value = "/authInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseResult<Object> successAuthorizeInfo(
            HttpServletResponse response, HttpServletRequest request) {
        try {
            //获取授权码 authorization_code
            String authorizationCode = request.getParameter("auth_code");
            log.info("auth_code:" + authorizationCode);
            //换取authorizerAccessToken
            BigInfo authAccessToken = weChatThridService.getAuthAccessToken(authorizationCode);
            // todo 保存相应信息

            // todo 开定时任务 刷新authAccessToken

            // todo 获取授权方信息

        } catch (Exception e) {
            log.info(e.getMessage());
            return ResultResponseUtils.error(ExceptionEnum.INVOKING_ERROR);
        }
        return ResultResponseUtils.success();
    }

}
