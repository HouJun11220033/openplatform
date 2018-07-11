package com.taikang.wechat.service.commponentVerifyTivket;


import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.VerifyTicket;

/**
 * AUTHOR 张清森
 * 方法说明：微信凭证业务层接口
 * DATE Created in  2018/7/10
 */
public interface VerifyTicketService {
    /**
     * 新增凭证数据
     * @param verifyTicket 凭证
     */
    void insertTicket(VerifyTicket verifyTicket);

    /**
     * 查询凭证
     * @return 凭证数据
     */
    VerifyTicket selectTicket();

    /**
     * 删除凭证
     */
    void deleteTicket();

}
