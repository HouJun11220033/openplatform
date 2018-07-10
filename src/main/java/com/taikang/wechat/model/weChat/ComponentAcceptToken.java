package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 令牌实体类
 * 张清森
 */
public class ComponentAcceptToken {
    /**
     * 主键id
     */
    private String componentAcceptTokenId;
    /**
     * 令牌
     */
    private String acceptToken;
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
