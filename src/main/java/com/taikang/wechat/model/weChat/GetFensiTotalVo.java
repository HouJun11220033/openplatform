package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFensiTotalVo implements Serializable {
    private static final long serialVersionUID = 7065129172347726385L;
    /**
     * 总数量
     */
    private Long total;
    /**
     * 拉取的OPENID个数，最大值为10000
     */
    private Long count;
}
