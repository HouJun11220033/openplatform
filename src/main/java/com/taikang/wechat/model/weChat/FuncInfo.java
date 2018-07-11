package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncInfo {
    /**
     * 功能集合
     */
    private List<FuncscopeCategory> funcscopeCategories;
}
