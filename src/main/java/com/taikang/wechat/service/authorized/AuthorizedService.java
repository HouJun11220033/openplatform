package com.taikang.wechat.service.authorized;

import com.taikang.wechat.model.weChat.AuthorizationInfo;
import com.taikang.wechat.model.weChat.BigAuthorizationInfo;
import com.taikang.wechat.model.weChat.WeChatComponentAccessTokenVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 微信授权
 * @author 张清森
 * @createTime 2018.7.9
 */
public interface AuthorizedService {
    /**
     * 处理微信授权事件
     * @param request 请求
     * @param response 响应
     */
    void handleAuthorize(HttpServletRequest request,
                         HttpServletResponse response) throws Exception;

    /**
     * 新增授权信息
     * @param bigAuthorizationInfo 授权信息
     */
    void insertAuthorService(BigAuthorizationInfo bigAuthorizationInfo);

    /**
     * 刷新令牌
     * @return 授权信息
     */
    AuthorizationInfo doRefreshToken(String authorizationInfoId);

    /**
     * 通过id查询授权信息
     * @param authorizationInfoId 授权信息主键id
     * @return 全部授权信息
     */
    BigAuthorizationInfo findBigAuthorizationInfoByid(String authorizationInfoId);

    /**
     * 更新授权信息
     * @param bigAuthorizationInfo 授权信息
     */
    void updateAuthorizationInfoById(BigAuthorizationInfo bigAuthorizationInfo);

    /**
     * 变更粉丝数量
     * @param toUserName appid
     * @param i 变更类型
     */
    void updateAuthorizationInfoFensiNumByAppid(String toUserName, int i);

    /**
     * 通过appid查询授权信息
     * @param toUserName appid
     * @return 授权信息
     */
    BigAuthorizationInfo getAuthorInfoByAppidService(String toUserName);
    /**
     * 通过userId 查询授权信息
     * @param userId 账户id
     * @return 授权信息
     */
    List<BigAuthorizationInfo> getAuthorInfoByUserIdService(String userId);
    /**
     * 通过appid 修改信息
     * @param bigAuthorizationInfo 授权信息
     */
    void updateAuthorizationInfoByAppId(BigAuthorizationInfo bigAuthorizationInfo);
}
