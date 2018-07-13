package com.taikang.wechat.model.weChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BigInfo implements Serializable {

    private static final long serialVersionUID = -2849274418407677601L;
    private AuthorizerInfo authorizer_info;
    private AuthorizationInfo authorization_info;

}
