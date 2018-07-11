package com.taikang.wechat.service.impl.authorizedimpl;

import com.alibaba.fastjson.JSON;
import com.taikang.wechat.config.handleexception.ControllerException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.*;
import com.taikang.wechat.service.authorized.AuthorizedService;
import com.taikang.wechat.service.commponentVerifyTivket.VerifyTicketService;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import com.taikang.wechat.service.precode.PreCodeService;
import com.taikang.wechat.utils.WeChatUtils;
import com.taikang.wechat.utils.aes.AesException;
import com.taikang.wechat.utils.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * 微信授权
 *
 * @author 张清森
 * 2018.7.9
 */
@Service
@Slf4j
public class AuthorizedServiceImpl implements AuthorizedService {
    private final VerifyTicketService ticketService;
    private final ComponentAcceptTokenService acceptTokenService;
    private final PreCodeService preCodeService;

    @Autowired
    public AuthorizedServiceImpl(VerifyTicketService ticketService, ComponentAcceptTokenService acceptTokenService, PreCodeService preCodeService) {
        this.ticketService = ticketService;
        this.acceptTokenService = acceptTokenService;
        this.preCodeService = preCodeService;
    }

    /**
     * 处理微信授权事件
     *
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void handleAuthorize(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        String timestamp = request.getParameter("timestamp");
        String encryptType = request.getParameter("encrypt_type");
        String nonce = request.getParameter("nonce");
        String msgSignature = request.getParameter("msg_signature");
        log.info("timestamp:" + timestamp);
        log.info("encrypt_type:" + encryptType);
        log.info("nonce:" + nonce);
        log.info("msg_signature:" + msgSignature);
        boolean checkSignature = WeChatUtils.checkSignature(WeChatContants.TOKEN, msgSignature, timestamp, nonce);
        if (!checkSignature) {
            log.info("签名错误");
            return;
        }
        //======================获取getComponentVerifyTicket===============//
        String componentVerifyTicket = getComponentVerifyTicket(request, timestamp, nonce, msgSignature);
        VerifyTicket verifyTicket = new VerifyTicket();
        verifyTicket.setComponentVerifyTicket(componentVerifyTicket);
        //保存凭证
        ticketService.insertTicket(verifyTicket);
        //=====================调用接口获取component_access_token=============//
        String componentAccessToken;
        //判断是否调用微信接口
        if (isGetAcceptToken()){
            WeChatComponentAccessTokenVo componentAccessTokenVo = getComponentAccessToken(componentVerifyTicket);
            componentAccessToken=componentAccessTokenVo.getComponent_access_token();
            ComponentAcceptToken componentAcceptToken = new ComponentAcceptToken();
            componentAcceptToken.setAcceptToken(componentAccessTokenVo.getComponent_access_token());
            componentAcceptToken.setExpiresIn(componentAccessTokenVo.getExpires_in());
            //保存令牌
            acceptTokenService.insertAcceptToken(componentAcceptToken);
        }else {
            ComponentAcceptToken componentAcceptTokenM = acceptTokenService.selectAcceptToken();
            componentAccessToken=componentAcceptTokenM.getAcceptToken();
        }
        //===================获取预授权码pre_auth_code*************************//
        Object[] object = {componentAccessToken};
        WeChatPreAuthCodeVo preAuthCodeVo = getWeChatPreAuthCodeVo(object);
        PreCode preCode = new PreCode();
        preCode.setPreCode(preAuthCodeVo.getPre_auth_code());
        preCode.setExpiresIn(preAuthCodeVo.getExpires_in());
        //将预授权码存在数据库中
        preCodeService.updatePreCodeByPreCodeId(preCode);
    }

    private boolean isGetAcceptToken() {
        ComponentAcceptToken componentAcceptTokenPre = acceptTokenService.selectAcceptToken();
        long t = System.currentTimeMillis();
        if (componentAcceptTokenPre != null) {
            Long begTime = componentAcceptTokenPre.getBegTime();
            Long expiresInPre = componentAcceptTokenPre.getExpiresIn();
            Long middle = t - begTime;
            Long aLong = (middle - expiresInPre);
            if (aLong.compareTo(new Long("12000")) <= 0) {
                return true;
            }
        }else {
            return true;
        }
        return false;
    }

    /**
     * 获取预授权码
     *
     * @param object 需要被替换的参数
     * @return WeChatPreAuthCodeVo
     */
    private WeChatPreAuthCodeVo getWeChatPreAuthCodeVo(Object[] object) {
        String preUrl = String.format(WeChatContants.THRID_PRE_AUTH_CODE, object);
        String result1 = WeChatUtils.postUrl(preUrl,
                WeChatGetPreAuthCodeVo.builder()
                        .component_appid(WeChatContants.THRID_APPID)
                        .build());
        log.info(result1);
        WeChatPreAuthCodeVo preAuthCodeVo;
        if (result1 != null && result1.contains("pre_auth_code")) {
            preAuthCodeVo = JSON.parseObject(result1, WeChatPreAuthCodeVo.class);
        } else {
            log.info("获取pre_auth_code失败！");
            throw new ControllerException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        return preAuthCodeVo;
    }

    /**
     * 获取令牌
     *
     * @param componentVerifyTicket 凭证
     * @return String
     */
    private WeChatComponentAccessTokenVo getComponentAccessToken(String componentVerifyTicket) {
        String url = WeChatContants.URL;
        String result = WeChatUtils.postUrl(url,
                WeChatThridGetTokenVo
                        .builder()
                        .component_appid(WeChatContants.THRID_APPID)
                        .component_appsecret(WeChatContants.THRID_APPSECRET)
                        .component_verify_ticket(componentVerifyTicket)
                        .build());
        log.info(result);
        WeChatComponentAccessTokenVo componentAccessTokenVo;
//        String componentAccessToken;
        if (result != null && result.contains("component_access_token")) {
            componentAccessTokenVo = JSON.parseObject(result, WeChatComponentAccessTokenVo.class);
//            componentAccessToken = componentAccessTokenVo.getComponent_access_token();
        } else {
            log.info("获取component_access_token失败！");
            throw new RuntimeException();
        }
        return componentAccessTokenVo;
    }

    /**
     * 获取凭证
     *
     * @param request      请求
     * @param timestamp    时间戳
     * @param nonce        附加信息
     * @param msgSignature 验证
     * @return String
     */

    private String getComponentVerifyTicket(HttpServletRequest request, String timestamp, String nonce, String msgSignature) throws IOException, AesException {
        String xml = getString(request);
        log.info("微信推送的原生：" + xml);
        // 第三方平台组件加密密钥
        String encodingAesKey = WeChatContants.ENCODING_AES_KEY;
        String appId = WeChatContants.THRID_APPID;
        String token = WeChatContants.TOKEN;

        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
        log.info("解密后的：" + xml);
        Map<String, String> parseXml = WeChatUtils.parseXml(xml);
        String componentVerifyTicket = parseXml.get("ComponentVerifyTicket");
        log.info(componentVerifyTicket);
        return componentVerifyTicket;
    }

    /**
     * 获取请求中的xml 变为String
     *
     * @param request 请求
     * @return String
     */
    private String getString(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
