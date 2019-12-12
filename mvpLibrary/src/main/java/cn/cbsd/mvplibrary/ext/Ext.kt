package cn.cbsd.mvplibrary.ext

import java.math.BigInteger
import java.security.MessageDigest

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-12-12 09:00
 */

/**
 * 获取非空字符串
 */
fun String?.getNotNull():String{
    return if (isNullOrEmpty()) ""
    else this!!
}

/**
 * 获取32位小写md5
 */
fun String.md5():String{
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}