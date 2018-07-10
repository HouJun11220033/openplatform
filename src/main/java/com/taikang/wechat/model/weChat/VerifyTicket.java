package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 凭证实体类
 * 张清森
 */
public class VerifyTicket {
    /**
     * 主键id
     */
    private String verifyTicketId;
    /**
     * 凭证码
     */
    private String componentVerifyTicket;
    /**
     * 有效时间
     */
   private Long expiresIn;
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
