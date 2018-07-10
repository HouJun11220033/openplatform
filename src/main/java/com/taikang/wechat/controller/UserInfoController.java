package com.taikang.wechat.controller;

import com.github.pagehelper.PageInfo;
import com.taikang.wechat.constant.ExceptionEnum;
import com.taikang.wechat.controller.DTO.UserInfoDTO;
import com.taikang.wechat.model.UserInfo;
import com.taikang.wechat.service.UserInfoService;
import com.taikang.wechat.utils.voutils.ResponseResult;
import com.taikang.wechat.utils.voutils.ResultResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("showController")
public class UserInfoController {

    //根据ID查询姓名
    private static final String SELECT_BY_ID = "selectById";
    //分页查询
    private static final String SELECT = "select";
    //=========================================
    //测试
    private static final String Find = "find";
    private final UserInfoService userInfoService;
    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }
    /**
     * @return  姓名
     * @param  userInfoDTO dto
     * @方法说明 通过ID查询姓名
     * @date 2018/7/5
     * @author 张清森
     */
    @PostMapping(SELECT_BY_ID)
    public ResponseResult<Object> selectByIdAction(@RequestBody UserInfoDTO userInfoDTO){
//        如果有必要切换域
//        UserInfo userInfo = new UserInfo();
//        BeanUtils.copyProperties(userInfoDTO,userInfo);
//        Integer id = userInfo.getId();
        Integer id = userInfoDTO.getId();
        if (id ==null) {
            return ResultResponseUtils.error(ExceptionEnum.INVOKING_ERROR);
        }
        //适用于无返回值
//        return ResultResponseUtils.success();
        return ResultResponseUtils.success(id);
    }
    @PostMapping(SELECT)
    public PageInfo<UserInfo> selectAction(){
        return userInfoService.select();
    }

    //自测
    @PostMapping(Find)
    public String getFind(){
        return "find";
    }
}
