package com.taikang.wechat.service.impl.authorizedimpl;

import com.alibaba.fastjson.JSON;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.WeChatComponentAccessTokenVo;
import com.taikang.wechat.model.weChat.WeChatGetPreAuthCodeVo;
import com.taikang.wechat.model.weChat.WeChatPreAuthCodeVo;
import com.taikang.wechat.model.weChat.WeChatThridGetTokenVo;
import com.taikang.wechat.service.authorized.AuthorizedService;
import com.taikang.wechat.utils.WeChatUtils;
import com.taikang.wechat.utils.aes.AesException;
import com.taikang.wechat.utils.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
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
        // todo boolean checkSignature = WeChatUtils.checkSignature(WeChatContants.TOKEN, msgSignature, timestamp, nonce);
        //======================调用接口获取getComponentVerifyTicket===============//
        String componentVerifyTicket = getComponentVerifyTicket(request, timestamp, nonce, msgSignature);
        //=====================调用接口获取component_access_token=============//
        String componentAccessToken = getComponentAccessToken(componentVerifyTicket);
        //===================获取预授权码pre_auth_code*************************//
        Object[] object = {componentAccessToken};
        String preUrl = String.format(WeChatContants.THRID_PRE_AUTH_CODE, object);
        String result1 = WeChatUtils.postUrl(preUrl,
                WeChatGetPreAuthCodeVo.builder()
                        .component_appid(WeChatContants.THRID_APPID)
                        .build());
        log.info(result1);
        String preAuthCode = "";
        if (result1 != null && result1.contains("pre_auth_code")) {
            WeChatPreAuthCodeVo preAuthCodeVo = JSON.parseObject(result1, WeChatPreAuthCodeVo.class);
            preAuthCode = preAuthCodeVo.getPre_auth_code();
            //将预授权码存在数据库中
            Map<String, Object> where = new HashMap<>(16);
            where.put("dic_type_id", "THRID_WECHAT_PRE_CODE");
//            QueryResult<CsDictVo> page = csDictService.findByPage(where);
//            if(page.getList()!=null&&page.getList().size()>0){
//                CsDictVo csDictVo = page.getList().get(0);
//                CsDict csDict=new CsDict();
//                BeanCopyPropertyUtils.copyProperties(csDict,csDictVo);
//                csDict.setDicCode(preAuthCode);
//                csDict.setDicValue(new Date().toString());
//                csDictService.update(csDict);
//            }
        } else {
            log.info("获取pre_auth_code失败！");
        }
    }

    /**
     * 获取令牌
     *
     * @param componentVerifyTicket 凭证
     * @return String
     */
    private String getComponentAccessToken(String componentVerifyTicket) {
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
        String componentAccessToken;
        if (result != null && result.contains("component_access_token")) {
            componentAccessTokenVo = JSON.parseObject(result, WeChatComponentAccessTokenVo.class);
            componentAccessToken = componentAccessTokenVo.getComponent_access_token();
        } else {
            log.info("获取component_access_token失败！");
            throw new RuntimeException();
        }
        return componentAccessToken;
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
