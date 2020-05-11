package cn.cbsd.mvplibrary.ext

import com.google.gson.reflect.TypeToken

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-12-12 09:04
 */

/**
 * 获取gson解析的泛型
 */
inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

inline fun <reified T> genericTypeToken() = object: TypeToken<T>() {}