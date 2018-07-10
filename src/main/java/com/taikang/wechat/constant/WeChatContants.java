package com.taikang.wechat.constant;

/**
 *
 */
public interface WeChatContants {
    String ENCODING_AES_KEY= "";
    String THRID_APPID = "";
    String TOKEN = "";
    String THRID_APPSECRET="";
    /**
     * 获取token地址
     */
    String URL="https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    /**
     * 获取预授权码地址
     */
    String THRID_PRE_AUTH_CODE="https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=%s";
}
