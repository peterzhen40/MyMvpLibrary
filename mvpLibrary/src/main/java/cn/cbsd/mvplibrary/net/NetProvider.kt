package cn.cbsd.mvplibrary.net

import me.jessyan.rxerrorhandler.core.RxErrorHandler
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * Created by wanglei on 2016/12/24.
 * 网络框架retrofit设置接口
 */

interface NetProvider {
    fun configInterceptors(): Array<Interceptor>

    fun configHttps(builder: OkHttpClient.Builder)

    fun configCookie(): CookieJar

    fun configConnectTimeoutMills(): Long

    fun configReadTimeoutMills(): Long

    fun configHandleError(isNeedShowToast: Boolean): RxErrorHandler
}
