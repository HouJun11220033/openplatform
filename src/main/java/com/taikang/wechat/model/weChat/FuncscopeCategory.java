package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncscopeCategory {
    /**
     * 功能id
     */
    private String id;
    /**
     * 描述
     */
    private String massage;
}
