package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 预授权码实体类
 * 张清森
 */
public class PreCode {
    /**
     * 主键id
     */
    private String preCodeId;
    /**
     * 预授权码
     */
    private String preCode;
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
