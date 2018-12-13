package cn.cbsd.mvplibrary.net;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by wanglei on 2016/12/24.
 * 网络框架retrofit设置接口
 */

public interface NetProvider {
    Interceptor[] configInterceptors();

    void configHttps(OkHttpClient.Builder builder);

    CookieJar configCookie();

    long configConnectTimeoutMills();

    long configReadTimeoutMills();

    RxErrorHandler configHandleError(boolean isNeedShowToast);
}
