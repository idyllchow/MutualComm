package com.sponia.foundationmoudle.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.utils
 * @description 验证各类输入是否合法
 * @date 15/9/25
 */
public class ValidateDataUtil {

    //电话号码
    public static final String REGEX_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}|([0-9])\\d{8}$";
    //姓名
    public static final String REGEX_NAME = "^[A-Za-z]{2,30}+$|^[\\u4e00-\\u9fa5]{2,11}$";
    //邮箱
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    //密码
    public static final String REGEX_PWD = "[^\\u4e00-\\u9fa5]{6,32}$";

    /**
     * 正则类型
     */
    public interface InputPattern {


        /**
         * 电话号码
         */
        Pattern PATTERN_PHONE = Pattern.compile(REGEX_PHONE);
        /**
         * 姓名
         */
        Pattern PATTERN_NAME = Pattern.compile(REGEX_NAME);
        /**
         * 邮箱
         */
        Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL);

        /**
         * 密码
         */
        Pattern PATTERN_PWD = Pattern.compile(REGEX_PWD);

    }

    /**
     * 验证输入
     * @param pattern
     * @param inuput
     * @return
     */
    public static boolean validateInput(Pattern pattern, String inuput) {
        Matcher matcher = pattern.matcher(inuput);
        return matcher.matches();
    }

}
