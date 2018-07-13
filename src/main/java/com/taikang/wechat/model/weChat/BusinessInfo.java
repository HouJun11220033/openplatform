package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessInfo {
    private Integer open_store;
    private Integer open_scan;
    private Integer open_pay;
    private Integer open_card;
    private Integer open_shake;
}
