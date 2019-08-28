/*
 * Copyright (c) 2018. 广州中爆数字信息科技股份有限公司.
 */

package cn.cbsd.mvplibrary.kit

import java.util.regex.Pattern

/**
 * 检验模板
 */
object PatternUtil {
    val REG_PHONE = "^1(3|4|5|7|8)[0-9]\\d{8}$"

    /**
     * 检验是否为手机
     *
     * @param phone
     * @return
     */
    fun checkPhone(phone: String): Boolean {
        val pattern = Pattern.compile(REG_PHONE)
        return pattern.matcher(phone).matches()
    }
}
