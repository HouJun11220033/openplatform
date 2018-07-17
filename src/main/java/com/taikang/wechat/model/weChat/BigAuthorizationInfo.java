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
public class BigAuthorizationInfo {
    /**
     * 主键id
     */
    private String authorizationInfoId;
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
     * 获取时间
     */
    private Long begTime;
    /**
     * 授权方名称
     */
    private String nick_name;
    /**
     * 授权方头像
     */
    private String head_img;
    /**
     * 授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
     */
    private Integer service_type;
    /**
     * 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，
     * 4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
     */
    private Integer verify_type;
    /**
     * 授权方公众号的原始ID
     */
    private String user_name;
    /**
     * 公众号的主体名称
     */
    private String principal_name;
    /**
     * 授权方公众号所设置的微信号，可能为空
     */
    private String alias;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 粉丝数量
     */
    private Long fensiNum;
    /**
     * 二维码url
     */
    private String qrcode_url;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 是否有效
     */
    private Integer isEffect;

}
