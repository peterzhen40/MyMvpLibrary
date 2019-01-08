package cn.cbsd.base.net.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 当前类注释:
 * Author: zhenyanjun
 * Date  : 2017/8/7 15:36
 */

public class SaveCookiesInterceptor implements Interceptor {

    private Context mContext;
    SharedPreferences sharedPreferences;

    public SaveCookiesInterceptor(Context context) {
        mContext = context;
        sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            sharedPreferences.edit()
                    .putStringSet("cookies", cookies)
                    .apply();
        }

        return originalResponse;
    }
}