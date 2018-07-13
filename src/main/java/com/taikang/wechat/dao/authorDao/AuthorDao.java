package com.taikang.wechat.dao.authorDao;


import com.taikang.wechat.model.weChat.AuthorizationInfo;
import com.taikang.wechat.model.weChat.BigAuthorizationInfo;
import com.taikang.wechat.model.weChat.PreCode;
import org.apache.ibatis.annotations.Mapper;


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
}
