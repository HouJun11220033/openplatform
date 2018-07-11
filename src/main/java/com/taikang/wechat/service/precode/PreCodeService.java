package com.taikang.wechat.service.precode;


import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.PreCode;

/**
 * AUTHOR 张清森
 * 方法说明：微信预授权码业务层接口
 * DATE Created in  2018/7/11
 */
public interface PreCodeService {
    /**
     * 新增预授权码数据
     * @param preCode 预授权码
     */
    void insertPreCode(PreCode preCode);

    /**
     * 查询预授权码
     * @return 预授权码数据
     */
    PreCode selectPreCode();

    /**
     * 删除预授权码
     */
    void deletePreCode();
    /**
     * 更新预授权码
     */
    void updatePreCodeByPreCodeId(PreCode preCode);
    /**
     * 先查后改
     */
    void selectAndUpdata(PreCode preCode);
}
