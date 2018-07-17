package com.taikang.wechat.service.wechatthiredservice.impl;


import com.alibaba.fastjson.JSON;
import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.ContantsEnum;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.*;
import com.taikang.wechat.service.authorized.AuthorizedService;
import com.taikang.wechat.service.componentAccessToken.ComponentAcceptTokenService;
import com.taikang.wechat.service.wechatthiredservice.WeChatThridService;
import com.taikang.wechat.utils.WeChatUtils;
import com.taikang.wechat.utils.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


/**
 * @author 张清森
 * 方法说明：微信凭证业务层接口
 * DATE Created in  2018/7/10
 */
@Service
@Slf4j
public class WeChatThirdServiceImpl implements WeChatThridService {
    private final ComponentAcceptTokenService componentAcceptTokenService;
    private final AuthorizedService authorizedService;

    @Autowired
    public WeChatThirdServiceImpl(ComponentAcceptTokenService componentAcceptTokenService
            , AuthorizedService authorizedService) {
        this.componentAcceptTokenService = componentAcceptTokenService;
        this.authorizedService = authorizedService;
    }

    /**
     * 获取授权码
     *
     * @param authorizationCode 授权码
     * @return 大对象
     */
    @Override
    public BigInfo getAuthAccessToken(String authorizationCode) {
        WeChatGetAuthorizationInfoVo
                .builder()
                .component_appid(WeChatContants.THRID_APPID)
                .authorization_code(authorizationCode)
                .build();

        String result = WeChatUtils.postUrl(WeChatContants.GET_AUTH_ACCESS_TOKEN, WeChatGetAuthorizationInfoVo
                .builder()
                .component_appid(WeChatContants.THRID_APPID)
                .authorization_code(authorizationCode)
                .build());
        return JSON.parseObject(result, BigInfo.class);
    }

    /**
     * 刷新 authorizer_access_token
     *
     * @param authorizationInfo 授权对象
     * @return 授权对象
     */
    @Override
    public AuthorizerInfo getAuthorizationInfo(AuthorizationInfo authorizationInfo) {
        //查询commtoken
        ComponentAcceptToken componentAcceptToken = componentAcceptTokenService.selectAcceptToken();
        String acceptToken = componentAcceptToken.getAcceptToken();
        WeChatGetAuthorInfo weChatGetAuthorInfo = new WeChatGetAuthorInfo();
        weChatGetAuthorInfo.setAuthorizer_appid(WeChatContants.THRID_APPID);
        weChatGetAuthorInfo.setComponent_appid(authorizationInfo.getAuthorizer_appid());
        //调用微信接口获取全部授权信息
        String format = String.format(WeChatContants.GET_AUTHOR_INFO, acceptToken);
        String authorInfo = WeChatUtils.postUrl(format, weChatGetAuthorInfo);
        BigInfo bigInfo = JSON.parseObject(authorInfo, BigInfo.class);
        return bigInfo.getAuthorizer_info();
    }

    /**
     * 获取某一公众号下的粉丝总数量
     *
     * @param bigAuthorizationInfo 公众号信息
     * @return 数量
     */
    @Override
    public Long getFensiTotal(BigAuthorizationInfo bigAuthorizationInfo) {
        if (bigAuthorizationInfo == null) {
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        String getFensiTotalUrl = WeChatContants.GET_FENSI_TOTAL_URL;

        Object[] vars = {bigAuthorizationInfo.getAuthorizer_access_token(), "0"};
        String format = String.format(getFensiTotalUrl, vars);
        String result = WeChatUtils.getUrl(format);
        GetFensiTotalVo getFensiTotalVo = JSON.parseObject(result, GetFensiTotalVo.class);
        if (getFensiTotalVo == null || getFensiTotalVo.getTotal() == null) {
            log.info("未能正确获取粉丝信息!");
            return 0L;
        }
        return getFensiTotalVo.getTotal();
    }

    /**
     * 处理用户事件
     *
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void handleMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //解密消息
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
        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        String xml = sb.toString();
        log.info("微信推送的原生：" + xml);
        // 第三方平台组件加密密钥
        String encodingAesKey = WeChatContants.ENCODING_AES_KEY;
        String appId = WeChatContants.THRID_APPID;
        String token = WeChatContants.TOKEN;

        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
        log.info("解密后的：" + xml);
        Map<String, String> parseXml = WeChatUtils.parseXml(xml);
        String toUserName = parseXml.get("ToUserName");
        String event = parseXml.get("Event");
        if (ContantsEnum.ENVEBT_TYPE_1.getCode().equals(event)) {
            authorizedService.updateAuthorizationInfoFensiNumByAppid(toUserName, 1);
        } else if (ContantsEnum.ENVEBT_TYPE_2.getCode().equals(event)) {
            authorizedService.updateAuthorizationInfoFensiNumByAppid(toUserName, 0);
        } else {
            log.info("完整");
        }
        //通过开发者微信号直接数量加减  todo 重新拉取用户数量

        log.info(toUserName);
        try (PrintWriter writer = response.getWriter()) {
            writer.write("success");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
