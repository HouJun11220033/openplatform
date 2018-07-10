package com.taikang.wechat.controller.applUser;

import com.taikang.wechat.controller.DTO.applUser.ApplUserDTO;
import com.taikang.wechat.controller.VO.ApplUser.ApplUserVO;
import com.taikang.wechat.service.applUserService.ApplUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhenghuan
 * @Date 2018/7/9 15:28
 * @Description 用户注册
 * @Version 1.0
 */
@RestController
@RequestMapping("applUserController")
public class ApplUserController {

    private static final String IS_VALIDATE_EMAIL = "isValidateEmail";

    @Autowired
    private ApplUserService lcApplService;

    public ApplUserController(ApplUserService lcApplService) {
        this.lcApplService = lcApplService;
    }

    @PostMapping(IS_VALIDATE_EMAIL)
    public ApplUserVO isValidateEmailAction(@RequestBody ApplUserDTO lcApplDTO){
        String userEmail = lcApplDTO.getUserEmail();
        return lcApplService.isUserEmail(userEmail);
    }
}
