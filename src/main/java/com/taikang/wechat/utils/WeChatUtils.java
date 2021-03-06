package com.taikang.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.taikang.wechat.config.handleexception.ControllerException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.constant.WeChatContants;
import com.taikang.wechat.model.weChat.WeChatComponentAccessTokenVo;
import com.taikang.wechat.model.weChat.WeChatGetPreAuthCodeVo;
import com.taikang.wechat.model.weChat.WeChatPreAuthCodeVo;
import com.taikang.wechat.model.weChat.WeChatThridGetTokenVo;
import com.taikang.wechat.utils.aes.AesException;
import com.taikang.wechat.utils.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信工具类
 */
@Slf4j
public class WeChatUtils {
    /**
     * 验证签名
     *
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce     nonce
     * @author zqs
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        // 将 token、timestamp、nonce 三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 sha1 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将 sha1 加密后的字符串可与 signature 对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray 字节数组
     * @author zqs
     */
    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte aByteArray : byteArray) {
            strDigest.append(byteToHexStr(aByteArray));
        }
        return strDigest.toString();
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte 字节
     * @author zqs
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * xml解析
     * @param xml 待解析字符串
     * @return map
     */
    public static Map<String, String> parseXml(String xml) throws AesException {
        Map<String, String> map = new HashMap<>(16);
        try (InputStream inputStream = new ByteArrayInputStream(xml.getBytes())){
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element element : elements) {
                map.put(element.getName(), element.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.ParseXmlError);
        }

        return map;
    }

    /**
     * 发送post请求
     * @param url 路径
     * @param obj 请求体
     * @return String
     */
    public static String postUrl(String url,Object obj){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String jsonString = JSON.toJSONString(obj);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonString, headers);
        try {
            return restTemplate.postForObject(url, formEntity, String.class);
        }catch (Exception e){
            log.info("路径有误");
            throw new RuntimeException();
        }
    }

    /**
     * 发送get请求
     * @param url 请求路径
     * @return json
     */
    public static String getUrl(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(url, String.class);
        }catch (Exception e){
            log.info("路径有误");
            throw new RuntimeException();
        }
    }
    /**
     * 获取令牌
     * @param componentVerifyTicket 凭证
     * @return String
     */
    public static WeChatComponentAccessTokenVo getComponentAccessToken(String componentVerifyTicket) {
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
        if (result != null && result.contains("component_access_token")) {
            componentAccessTokenVo = JSON.parseObject(result, WeChatComponentAccessTokenVo.class);
        } else {
            log.info("获取component_access_token失败！");
            throw new RuntimeException();
        }
        return componentAccessTokenVo;
    }

    /**
     * 获取预授权码
     * @param object 需要被替换的参数
     * @return WeChatPreAuthCodeVo
     */
    public static WeChatPreAuthCodeVo getWeChatPreAuthCodeVo(Object[] object) {
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

    public static Map<String, String> getStringXmlMap(String timestamp, String nonce, String msgSignature, String xml) throws AesException {

            String encodingAesKey = WeChatContants.ENCODING_AES_KEY;
            String appId = WeChatContants.THRID_APPID;
            String token = WeChatContants.TOKEN;
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
            log.info("解密后的：" + xml);
            return WeChatUtils.parseXml(xml);

    }
    /**
     * 统一回复微信服务器
     * @param response 响应
     * @param content 参数
     */
    public static void responseReplyMessage(HttpServletResponse response, String content) throws IOException {
        PrintWriter pw = response.getWriter();
        pw.write(content);
        pw.flush();
        pw.close();
    }
}
