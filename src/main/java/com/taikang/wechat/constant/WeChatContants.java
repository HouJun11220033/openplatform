package com.taikang.wechat.constant;

/**
 *
 */
public interface WeChatContants {
    /**
     * 秘钥
     */
    String ENCODING_AES_KEY= "";
    /**
     * appid
     */
    String THRID_APPID = "";
    /**
     * token
     */
    String TOKEN = "";
    /**
     * 秘钥
     */
    String THRID_APPSECRET="";
    /**
     * token 有效时间
     */
    Long TOKEN_EXPERI_IN=600L;
    /**
     * 自己的域名 "http://自己的域名.com/a/weixin2/authInfo/{userId}"
     */
    String OWN_SCOPE="";
    /**
     * 获取token地址
     */
    String URL="https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    /**
     * 获取预授权码地址
     */
    String THRID_PRE_AUTH_CODE="https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=%s";
    /**
     * 授权网页地址
     */
    String THRID_AUTHORIZATION_CODE="https://mp.weixin.qq.com/cgi-bin/componentloginpage?component\\_appid=%s&pre\\_auth\\_code=%s&redirect\\_uri=%s&auth\\_type=3";
    /**
     * 获取AuthAccessToken路径
     */
    String GET_AUTH_ACCESS_TOKEN="https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=%s";
    /**
     * 获取授权信息路径
     */
    String GET_AUTHOR_INFO = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=%s";
    /**
     * 刷新authorizer_access_token路径
     */
    String REFRESH_AUTHORIZER_ACCESS_TOKEN="https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=%s";
    /**
     * 获取公众号粉丝列表路径
     */
    String GET_FENSI_TOTAL_URL="https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";
}
