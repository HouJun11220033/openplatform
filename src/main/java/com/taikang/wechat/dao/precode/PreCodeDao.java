package com.taikang.wechat.dao.precode;


import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.PreCode;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author: 张清森
 * @description 数据访问层接口
 * @date: Created in 14:28 2018/7/111
 */
@Mapper
public interface PreCodeDao {

    /**
     * 新增预授权码数据
     *
     * @param preCode 预授权码
     */
    void insertPreCodeDao(PreCode preCode);

    /**
     * 查询预授权码
     *
     * @return 预授权码
     */
    PreCode selectProCodeDao();

    /**
     * 删除预授权码
     */
    void deletePreCodeDao(String id);
    /**
     * 更新预授权码
     */
    void updatePreCodeByPreCodeId(PreCode preCode);
}
