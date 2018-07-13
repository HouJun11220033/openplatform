package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 授权信息
 * 张清森
 */
public class AuthorizationInfo {

    /**
     * 公众号id
     */
    private String authorizer_appid;
    private String authorization_appid;
    /**
     * 授权令牌
     */
    private String authorizer_access_token;
    /**
     * 有效时间
     */
    private Long expires_in;

    /**
     * 刷新token
     */
    private String authorizer_refresh_token;
    /**
     * 权限集
     */
    private List<FuncInfo> func_info;

}
