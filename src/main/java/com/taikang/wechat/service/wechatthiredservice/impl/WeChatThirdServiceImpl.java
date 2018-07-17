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
import com.taikang.wechat.utils.aes.AesException;
import com.taikang.wechat.utils.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Calendar;
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

        //微信全网发布公测
        String msgType = parseXml.get("MsgType");
        String fromUserName = parseXml.get("FromUserName");
        if ("event".equals(msgType)) {
            log.info("---------------事件消息--------");
            replyEventMessage(request, response, event, toUserName, fromUserName);
        } else if ("text".equals(msgType)) {
            log.info("---------------文本消息--------");
            String content = parseXml.get("Content");
            processTextMessage(request, response, content, toUserName, fromUserName);
        }

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


    /**
     * 微信全网接入  事件消息
     *
     * @param request
     * @param response
     * @param event
     * @param toUserName
     * @param fromUserName
     * @throws Exception
     */
    private void replyEventMessage(HttpServletRequest request, HttpServletResponse response, String event, String toUserName, String fromUserName) throws Exception {
        String content = event + "from_callback";
        log.info("--------------事件回复消息  content=" + content + "   toUserName=" + toUserName + "   fromUserName=" + fromUserName);
        replyTextMessage(request, response, content, toUserName, fromUserName);
    }

    /**
     * 微信全网接入  文本消息
     *
     * @param request
     * @param response
     * @param toUserName
     * @param fromUserName
     */
    private void processTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName) throws Exception {
        if ("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content)) {
            String returnContent = content + "_callback";
            replyTextMessage(request, response, returnContent, toUserName, fromUserName);
        } else if (StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE")) {
            //先回复空串
            WeChatUtils.responseReplyMessage(response, "");
            //接下来客服API再回复一次消息
            replyApiTextMessage(request, response, content.split(":")[1], fromUserName, 1);
        }
    }

    /**
     * 回复微信服务器"文本消息"
     *
     * @param request
     * @param response
     * @param content
     * @param toUserName
     * @param fromUserName
     * @throws IOException
     */
    public void replyTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName) throws Exception {
        Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>");
        sb.append("<FromUserName><![CDATA[" + toUserName + "]]></FromUserName>");
        sb.append("<CreateTime>" + createTime + "</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA[" + content + "]]></Content>");
        sb.append("</xml>");
        String replyMsg = sb.toString();

        String returnvaleue = "";
        try {
            // 第三方平台组件加密密钥
            String encodingAesKey = WeChatContants.ENCODING_AES_KEY;
            //从xml中解析
            String appId = WeChatContants.THRID_APPID;
            WXBizMsgCrypt pc = new WXBizMsgCrypt(WeChatContants.TOKEN, encodingAesKey, appId);
            returnvaleue = pc.encryptMsg(replyMsg, createTime.toString(), request.getParameter("nonce"));
            log.info("------------------加密后的返回内容 returnvaleue： " + returnvaleue);
        } catch (AesException e) {
            e.printStackTrace();
        }
        WeChatUtils.responseReplyMessage(response, returnvaleue);
    }

    /**
     * 客服接口回复粉丝信息
     *
     * @param response
     * @param auth_code    当type=1是才有值，type=0为null
     * @param fromUserName
     * @param type         1 表示全网发布时回复
     *                     0 表示普通消息回复
     * @throws Exception
     */
    public void replyApiTextMessage(HttpServletRequest request, HttpServletResponse response, String auth_code, String fromUserName, int type) throws Exception {
        //从数据库中获取access_token
        BigAuthorizationInfo big = authorizedService.getAuthorInfoByAppidService("wx570bc396a51b8ff8");
        String access_token = big.getAuthorizer_access_token();
        //模拟客户回复文本消息
        Object[] objects = {access_token};
        String sendMessageTextUrl = String.format(WeChatContants.THRID_KEFU_SENDMESSAGE_URL, objects);
        //组装post数据
        WeChatKeFuSendTextMessageVo textmessageVo = new WeChatKeFuSendTextMessageVo();
        textmessageVo.setMsgtype("text");
        textmessageVo.setTouser(fromUserName);
        WeChatKeFuSendTextVo textContentVo = new WeChatKeFuSendTextVo();
        String textContent = "";
        if (type == 1) {
            //全网发布回复的内容
            textContent = auth_code + "_from_api";
        } else {
            //普通文本消息回复的内容
            textContent = "hello,ok!";
        }
        textContentVo.setContent(textContent);
        textmessageVo.setText(textContentVo);
//        String result = HttpNetUtils.getInstance().httpByJson(sendMessageTextUrl,"POST",textmessageVo);
        String result = WeChatUtils.postUrl(sendMessageTextUrl, textmessageVo);
        log.info("客服回复结果：" + result);
    }

}
