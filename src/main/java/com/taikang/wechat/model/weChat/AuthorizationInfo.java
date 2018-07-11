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
     * 主键id
     */
    private String AuthorizationInfoId;
    /**
     * 公众号id
     */
    private String authorizer_appid;
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
    private FuncInfo func_info;
    /**
     * 获取时间
     */
    private Long begTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}
