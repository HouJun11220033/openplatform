package com.taikang.wechat.service.applUserServiceImpl;

import com.taikang.wechat.controller.VO.ApplUser.ApplUserVO;
import com.taikang.wechat.dao.applUser.ApplUserDao;
import com.taikang.wechat.model.utils.ValidateUtil;
import com.taikang.wechat.service.applUserService.ApplUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhenghuan
 * @Date 2018/7/9 10:56
 * @Description
 * @Version 1.0
 */
@Service
@Slf4j
public class ApplUserServiceImpl implements ApplUserService {


    private final ApplUserDao applUserDao;

    @Autowired
    public ApplUserServiceImpl(ApplUserDao applUserDao) {
        this.applUserDao = applUserDao;
    }

    /**
     * @Author zhenghuan
     * @Date 2018/7/9 10:58
     * @Description 校验邮箱
     * @Version 1.0
     */
    public ApplUserVO isUserEmail(String userEmail){
        ApplUserVO lcApplVO = new ApplUserVO();
        if (userEmail != null && userEmail.length() != 0) {
            if (ValidateUtil.isValidEmail(userEmail)) {//有效的邮箱
                //邮箱是否已注册
                if (applUserDao.isExistEmaile(userEmail) != null) {
                    lcApplVO.setId("1");
                    lcApplVO.setMessage("fail");
                }else {
                    lcApplVO.setId("2");
                    lcApplVO.setMessage("success");
                }
            }else{
                lcApplVO.setId("3");
                lcApplVO.setMessage("邮箱格式不正确");
            }
        } else {
            lcApplVO.setId("4");
            lcApplVO.setMessage("传入的参数值userEmail为空");
        }
        return  lcApplVO;
    }

}
