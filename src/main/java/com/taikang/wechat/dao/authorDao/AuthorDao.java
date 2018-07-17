package com.taikang.wechat.dao.authorDao;


import com.taikang.wechat.model.weChat.AuthorizationInfo;
import com.taikang.wechat.model.weChat.BigAuthorizationInfo;
import com.taikang.wechat.model.weChat.PreCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author: 张清森
 * @description 数据访问层接口
 * @date: Created in 14:28 2018/7/12
 */
@Mapper
public interface AuthorDao {

    /**
     * 新增授权方数据
     *
     * @param bigAuthorizationInfo 授权方数据
     */
    void insertAuthorDao(BigAuthorizationInfo bigAuthorizationInfo);

    BigAuthorizationInfo findAuthorizationInfoByIdDao(String authorizationInfoId);
    /**
     * 通过id查询授权信息
     * @param authorizationInfoId 授权信息主键id
     * @return 全部授权信息
     */
    BigAuthorizationInfo findBigAuthorizationInfoByIdDao(String authorizationInfoId);
    /**
     * 更新授权信息
     * @param bigAuthorizationInfo 授权信息
     */
    void updateAuthorizationInfoByIdDao(BigAuthorizationInfo bigAuthorizationInfo);

    /**
     * 更新粉丝数量
     * @param bigAuthorizationInfo 授权信息
     */
    void updateAuthorizationInfoFensiNumByAppid(BigAuthorizationInfo bigAuthorizationInfo);

    /**
     * 通过appid查询授权信息
     * @param toUserName appid
     * @return 授权信息
     */
    BigAuthorizationInfo getAuthorInfoByAppidDao(String toUserName);

    /**
     * 通过appid 修改信息
     * @param bigAuthorizationInfo 授权信息
     */
    void updateAuthorizationInfoByAppIdDao(BigAuthorizationInfo bigAuthorizationInfo);
    /**
     * 通过userId 查询授权信息
     * @param userId 账户id
     * @return 授权信息
     */
    List<BigAuthorizationInfo> getAuthorInfoByUserIdDao(String userId);
}
