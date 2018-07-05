package com.taikang.wechat.controller;

import com.taikang.wechat.controller.DTO.UserInfoDTO;
import com.taikang.wechat.model.UserInfo;
import com.taikang.wechat.service.ShowService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("showController")
public class ShowController {

    //根据ID查询姓名
    private static final String SELECT_BY_ID = "selectById";

    //=========================================
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }
    /**
     * @return  姓名
     * @param  userInfoDTO dto
     * @方法说明 通过ID查询姓名
     * @date 2018/7/5
     * @author 张清森
     */
    @PostMapping(SELECT_BY_ID)
    public String selectByIdAction(@RequestBody UserInfoDTO userInfoDTO){
//        如果有必要切换域
//        UserInfo userInfo = new UserInfo();
//        BeanUtils.copyProperties(userInfoDTO,userInfo);
//        Integer id = userInfo.getId();
        Integer id = userInfoDTO.getId();
        if (id ==null) return "error";
        return showService.selectByIdService(id);
    }
}
