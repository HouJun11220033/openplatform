package com.taikang.wechat.service.impl;

import com.taikang.wechat.dao.ShowDao;
import com.taikang.wechat.service.ShowService;
import org.springframework.stereotype.Service;

/**
 * AUTHOR nicai
 * 方法说明：代码展示业务层接口
 * DATE Created in  2018/7/5
 */
@Service
public class ShowServiceImpl implements ShowService {
    private final ShowDao showDao;

    public ShowServiceImpl(ShowDao showDao) {
        this.showDao = showDao;
    }

    /**
     * @return  姓名
     * @param  id 主键id
     * @方法说明 通过ID查询姓名
     * @date 2018/7/5
     * @author 张清森
     */
    @Override
    public String selectByIdService(Integer id) {
//        Page<Object> objects = PageHelper.startPage(0, 10)
        if (id==null) return null;;
        return showDao.selectNameById(id);
    }
}
