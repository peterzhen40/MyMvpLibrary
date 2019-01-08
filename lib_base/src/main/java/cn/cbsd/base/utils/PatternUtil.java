/*
 * Copyright (c) 2018. 广州中爆数字信息科技股份有限公司.
 */

package cn.cbsd.base.utils;

import java.util.regex.Pattern;

/**
 * 检验模板
 */
public class PatternUtil {
    public static final String REG_PHONE = "^1(3|4|5|7|8)[0-9]\\d{8}$";

    /**
     * 检验是否为手机
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile(REG_PHONE);
        return pattern.matcher(phone).matches();
    }
}
