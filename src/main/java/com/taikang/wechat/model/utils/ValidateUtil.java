package com.taikang.wechat.model.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhenghuan
 * @Date 2018/7/9 13:39
 * @Description 校验邮箱手机号工具类
 * @Version 1.0
 */
public class ValidateUtil {

    /**
     * @Author zhenghuan
     * @Date 2018/7/9 13:40
     * @Description 邮箱是否有效
     * @Version 1.0
     * @param email
     * @return flag
     */
    public static boolean isValidEmail(String email) {
        String reg = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern pattern = Pattern.compile(reg);
        boolean flag = false;
        if(email != null && email.length() != 0) {
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        }
        return  flag;
    }

    /**
     * @Author zhenghuan
     * @Date 2018/7/9 13:40
     * @Description 手机号是否有效
     * @Version 1.0
     * @param userPhone
     * @return flag
     */
    public static boolean isValidPhone(String userPhone) {
        String reg = "0?(13|14|15|18)[0-9]{9}";
        Pattern pattern = Pattern.compile(reg);
        boolean flag = false;
        if(userPhone != null && userPhone.length() != 0) {
            Matcher matcher = pattern.matcher(userPhone);
            flag = matcher.matches();
        }
        return  flag;
    }

}
