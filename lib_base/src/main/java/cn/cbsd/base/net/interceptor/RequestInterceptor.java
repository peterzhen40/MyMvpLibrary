package cn.cbsd.base.net.interceptor;

import java.io.IOException;

import cn.cbsd.base.utils.CommonKit;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * 当前类注释:请求拦截器
 * Author: zhenyanjun
 * Date  : 2017/9/26 09:20
 */

public class RequestInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String uniqueID = CommonKit.getUniquePsuedoID();
        Timber.d(uniqueID);
        Request request = original.newBuilder()
                .header("category", "Android")
                .header("version","1")
                .header("tercode", uniqueID)
                //.header("authorization-token", LoginSp.getToken(AppKit.getContext()))
                .method(original.method(), original.body())
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
