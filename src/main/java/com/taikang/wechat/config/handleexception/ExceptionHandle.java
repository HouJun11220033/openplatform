package com.taikang.wechat.config.handleexception;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.taikang.wechat.constant.ExceptionEnum;
import com.taikang.wechat.constant.IExceptionEnum;
import com.taikang.wechat.utils.voutils.ResponseResult;
import com.taikang.wechat.utils.voutils.ResultResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ModelAndView Handle(Exception e) {
        ModelAndView mv = new ModelAndView();
        /*
         * 使用 FastJson 提供的 FastJsonJsonView 视图返回，不需要捕获异常
         */
        IExceptionEnum responseEnum;
        if (e instanceof ServiceException){
            ServiceException serviceException = (ServiceException) e;
            responseEnum = serviceException.getResponseEnum();
        }else if (e instanceof ControllerException){
            ControllerException controllerException = (ControllerException) e;
            responseEnum = controllerException.getResponseEnum();
        }else {
            responseEnum=ExceptionEnum.SYS_ERROR;
        }
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<>(16);
        attributes.put("code", "1000001");
        attributes.put("msg", responseEnum.getMessage());
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
//        if (e instanceof ServiceException) {
//            ServiceException serviceException = (ServiceException) e;
//            IExceptionEnum responseEnum = serviceException.getResponseEnum();
//            return ResultResponseUtils.error(responseEnum);
//        }else {
//            return ResultResponseUtils.error(ExceptionEnum.SYS_ERROR);
//        }
    }

}