package cn.cbsd.mvplibrary.kit

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * 当前类注释:
 * Author: zhenyanjun
 * Date  : 2017/8/7 11:16
 */

object MD5Util {
    /**
     * @param str
     * @return
     * @Date: 2013-9-6
     * @Author: lulei
     * @Description:  32位小写MD5
     */
    @JvmStatic
    fun parseStrToMd5L32(str: String): String? {
        var reStr: String? = null
        try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(str.toByteArray())
            val stringBuffer = StringBuffer()
            for (b in bytes) {
                val bt = b and 0xff.toByte()
                if (bt < 16) {
                    stringBuffer.append(0)
                }
                stringBuffer.append(Integer.toHexString(bt.toInt()))
            }
            reStr = stringBuffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return reStr
    }

    /**
     * @param str
     * @return
     * @Date: 2013-9-6
     * @Author: lulei
     * @Description: 32位大写MD5
     */
    @JvmStatic
    fun parseStrToMd5U32(str: String): String? {
        var reStr = parseStrToMd5L32(str)
        if (reStr != null) {
            reStr = reStr.toUpperCase()
        }
        return reStr
    }

    /**
     * @param str
     * @return
     * @Date: 2013-9-6
     * @Author: lulei
     * @Description: 16位小写MD5
     */
    @JvmStatic
    fun parseStrToMd5U16(str: String): String? {
        var reStr = parseStrToMd5L32(str)
        if (reStr != null) {
            reStr = reStr.toUpperCase().substring(8, 24)
        }
        return reStr
    }

    /**
     * @param str
     * @return
     * @Date: 2013-9-6
     * @Author: lulei
     * @Description: 16位大写MD5
     */
    @JvmStatic
    fun parseStrToMd5L16(str: String): String? {
        var reStr = parseStrToMd5L32(str)
        if (reStr != null) {
            reStr = reStr.substring(8, 24)
        }
        return reStr
    }
}
