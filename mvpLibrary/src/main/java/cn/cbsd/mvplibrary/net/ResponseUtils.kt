package cn.cbsd.mvplibrary.net

import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * 当前类注释: response处理和解析
 * @author zhenyanjun
 * @date   2020/5/8 16:14
 */
//fun <T> Response<ResponseBody>.parseXml(clazz: Class<T>): T {
//    if (isSuccessful) {
//        val body = body()
//        if (body != null) {
//            val contentType = body.contentType()
//            return when (contentType?.subtype) {
//                "xml", "html" -> {
//                    //xml解析
//                    try {
//                        Persister().read(clazz, body.charStream(), false)
//                    } catch (e: Exception) {
//                        throw Exception("xml解析失败：${e.message}")
//                    }
//                }
//                else -> throw Exception("返回值内容非xml")
//            }
//        } else {
//            throw IOException("返回值内容为空")
//        }
//    } else {
//        throw HttpException(this)
//    }
//}

fun <T> Response<ResponseBody>.parseJson(typeToken: TypeToken<T>): T {
    if (isSuccessful) {
        val body = body()
        if (body != null) {
            val contentType = body.contentType()
            return when (contentType?.subtype()) {
                "json" -> {
                    //json解析
                    //不能提前调用，string()这方法只能调用一次，之后就close没数据了
                    val string = body.string()
                    try {
                        GsonProvider.gson.fromJson<T>(string, typeToken.type)
                    } catch (e: Exception) {
                        throw Exception("json解析失败：${e.message}")
                    }
                }
                else -> throw Exception("返回值内容非xml或json，解析不了")
            }
        } else {
            throw IOException("返回值内容为空")
        }
    } else {
        throw HttpException(this)
    }
}

