package com.taikang.wechat.dao.componentaccepttoken;


import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.VerifyTicket;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author: 张清森
 * @description 数据访问层接口
 * @date: Created in 14:28 2018/7/10
 */
@Mapper
public interface ComponentAcceptTokenDao {

    /**
     * 新增token数据
     *
     * @param componentAcceptToken 令牌
     */
    void insertAcceptTokenDao(ComponentAcceptToken componentAcceptToken);

    /**
     * 查询令牌
     *
     * @return 凭证令牌
     */
    ComponentAcceptToken selectAcceptToken();

    /**
     * 删除令牌
     */
    void deleteAcceptToken(String id);
}
