package com.taikang.wechat.service.precode.impl;


import com.taikang.wechat.config.handleexception.ServiceException;
import com.taikang.wechat.constant.HRSCExceptionEnum;
import com.taikang.wechat.dao.precode.PreCodeDao;
import com.taikang.wechat.model.weChat.ComponentAcceptToken;
import com.taikang.wechat.model.weChat.PreCode;
import com.taikang.wechat.service.precode.PreCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * AUTHOR 张清森
 * 方法说明：微信预授权码业务层接口
 * DATE Created in  2018/7/11
 */
@Service
@Slf4j
public class PreCodeServiceImpl implements PreCodeService {

    private final PreCodeDao preCodeDao;

    @Autowired
    public PreCodeServiceImpl(PreCodeDao preCodeDao) {
        this.preCodeDao = preCodeDao;
    }

    /**
     * 新增预授权码数据
     *
     * @param preCode 预授权码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertPreCode(PreCode preCode) {
        preCode.setBegTime(System.currentTimeMillis());
        preCode.setCreateTime(new Date());
        preCode.setPreCodeId("pre_code");
        preCode.setUpdateTime(new Date());
        preCodeDao.insertPreCodeDao(preCode);
    }

    /**
     * 查询预授权码
     *
     * @return 预授权码数据
     */
    @Override
    public ComponentAcceptToken selectPreCode() {

        return preCodeDao.selectProCodeDao();
    }

    /**
     * 删除预授权码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePreCode() {

    }

    /**
     * 更新预授权码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreCodeByPreCodeId(PreCode preCode) {
        if (preCode == null||preCode.getPreCodeId()==null) {
            throw new ServiceException(HRSCExceptionEnum.PARAMGRAM_MISS);
        }
        preCode.setUpdateTime(new Date());
        preCode.setBegTime(System.currentTimeMillis());
        preCodeDao.updatePreCodeByPreCodeId(preCode);
    }

    /**
     * 先查后改
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAndUpdata(PreCode preCode) {
        ComponentAcceptToken componentAcceptToken = this.selectPreCode();
        if (componentAcceptToken!=null){
            this.updatePreCodeByPreCodeId(preCode);
        }else {
            this.insertPreCode(preCode);
        }
    }
}
