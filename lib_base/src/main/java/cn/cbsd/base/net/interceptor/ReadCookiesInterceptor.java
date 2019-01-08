package cn.cbsd.base.net.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * 当前类注释:
 * Author: zhenyanjun
 * Date  : 2017/8/7 15:33
 */

public class ReadCookiesInterceptor implements Interceptor {

    private Context context;

    public ReadCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        HashSet<String> preferences = (HashSet<String>) sharedPreferences.getStringSet("cookies", new HashSet<>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Timber.v("Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}
