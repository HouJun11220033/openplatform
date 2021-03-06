package com.taikang.wechat.controller.author;


import com.taikang.wechat.config.handleexception.ControllerException;
import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.ExceptionEnum;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.*;
import com.taikang.wechat.service.authorized.AuthorizedService;
import com.taikang.wechat.service.precode.PreCodeService;
import com.taikang.wechat.service.wechatthiredservice.WeChatThridService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;


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
    private final AuthorizedService authorizedService;

    @Autowired
    public AuthorController(PreCodeService preCodeService, WeChatThridService weChatThridService, AuthorizedService authorizedService) {
        this.preCodeService = preCodeService;
        this.weChatThridService = weChatThridService;
        this.authorizedService = authorizedService;
    }

    /**
     * 授权页面
     */
    @GetMapping(value = "goAuthPage/{userId}")
    public void acceptAuthorizeEvent(HttpServletResponse response, @PathVariable(value = "userId") String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException(HRSCExceptionEnum.USER_ID_MISS);
        }
        try {
            //查找预授权码
            PreCode preAuthCode = preCodeService.selectPreCode();
            if (preAuthCode == null) {
                throw new ServiceException(HRSCExceptionEnum.PRE_AUTH_CODE_MISS);
            }
            //********************跳转到授权页面********************************//
            Object[] args = {WeChatContants.THRID_APPID, preAuthCode.getPreCode(), WeChatContants.OWN_SCOPE};
            String authorizationUrl = String.format(WeChatContants.THRID_AUTHORIZATION_CODE, args);
            log.info("跳转的URL：" + authorizationUrl);
            response.setStatus(301);
            response.sendRedirect(authorizationUrl);
        } catch (Exception e) {
            log.info("调用失败!");
        }
    }

    /**
     * 处理微信授权事件完成跳转URL
     */
    @RequestMapping(value = "/authInfo/{userId}", method = {RequestMethod.GET, RequestMethod.POST})
    public void successAuthorizeInfo(
            HttpServletResponse response, HttpServletRequest request, @PathVariable(value = "userId") String userId) {
        if (StringUtils.isEmpty(userId)){
            log.info("userId为空");
            throw new ServiceException(HRSCExceptionEnum.USER_ID_MISS);
        }
        try {
            //获取授权码 authorization_code
            String authorizationCode = request.getParameter("auth_code");
            log.info("auth_code:" + authorizationCode);
            //换取authorizerAccessToken
            BigInfo bigInfo = weChatThridService.getAuthAccessToken(authorizationCode);
            //第一次获取授权信息
            AuthorizationInfo authorizationInfo1 = bigInfo.getAuthorization_info();
            // 第二次获取授权方信息
            AuthorizerInfo authorizationInfo2 = weChatThridService.getAuthorizationInfo(authorizationInfo1);
            // 整合两次授权信息
            BigAuthorizationInfo bigAuthorizationInfo = combainBigAuthorizationInfo(authorizationInfo1, authorizationInfo2);
            //调用粉丝接口
            Long fensiTotal = weChatThridService.getFensiTotal(bigAuthorizationInfo);
            bigAuthorizationInfo.setFensiNum(fensiTotal);
            //保存相应信息
            authorizedService.insertAuthorService(bigAuthorizationInfo);
            bigAuthorizationInfo.setUserId(userId);
            //todo 跳转授权成功页面
//            response.setStatus(301);
//            response.sendRedirect("");
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new ControllerException(ExceptionEnum.INVOKING_ERROR);
        }
    }

    /**
     * 整合两次授权信息
     *
     * @param authorizationInfo1 第一次授权信息
     * @param authorizationInfo2 第二次授权信息
     * @return 整合之后的结果
     */
    private BigAuthorizationInfo combainBigAuthorizationInfo(AuthorizationInfo authorizationInfo1, AuthorizerInfo authorizationInfo2) {
        String authorizerAccessToken = authorizationInfo1.getAuthorizer_access_token();
        String authorizerAppid = authorizationInfo1.getAuthorizer_appid();
        String authorizerRefreshToken = authorizationInfo1.getAuthorizer_refresh_token();
        Long expiresIn = authorizationInfo1.getExpires_in();

        String nickName = authorizationInfo2.getNick_name();
        String headImg = authorizationInfo2.getHead_img();
        String alias = authorizationInfo2.getAlias();
        String principalName = authorizationInfo2.getPrincipal_name();
        String userName = authorizationInfo2.getUser_name();
        String qrcodeUrl = authorizationInfo2.getQrcode_url();
        Integer serviceType = authorizationInfo2.getService_type_info().getId();
        Integer verifyType = authorizationInfo2.getVerify_type_info().getId();
        long begin = System.currentTimeMillis();
        String id = UUID.randomUUID().toString();
        BigAuthorizationInfo bigAuthorizationInfo = new BigAuthorizationInfo();
        bigAuthorizationInfo.setAlias(alias);
        bigAuthorizationInfo.setAuthorizer_appid(authorizerAppid);
        bigAuthorizationInfo.setAuthorizationInfoId(UUID.randomUUID().toString());
        bigAuthorizationInfo.setAuthorizer_access_token(authorizerAccessToken);
        bigAuthorizationInfo.setAuthorizer_refresh_token(authorizerRefreshToken);
        bigAuthorizationInfo.setExpires_in(expiresIn);
        bigAuthorizationInfo.setNick_name(nickName);
        bigAuthorizationInfo.setHead_img(headImg);
        bigAuthorizationInfo.setPrincipal_name(principalName);
        bigAuthorizationInfo.setUser_name(userName);
        bigAuthorizationInfo.setQrcode_url(qrcodeUrl);
        bigAuthorizationInfo.setService_type(serviceType);
        bigAuthorizationInfo.setVerify_type(verifyType);
        bigAuthorizationInfo.setCreateTime(new Date());
        bigAuthorizationInfo.setBegTime(begin);
        bigAuthorizationInfo.setAuthorizationInfoId(id);
        return bigAuthorizationInfo;
    }
}
