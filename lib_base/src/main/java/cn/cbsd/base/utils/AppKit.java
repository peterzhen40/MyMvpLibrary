package cn.cbsd.base.utils;

import android.content.Context;

/**
 * 当前类注释:配置application全局静态变量
 * @author zhenyanjun
 * @date   2019/1/8 10:08
 */

public class AppKit {
    private static Context sContext;

    public static void init(Context applicationContext) {
        sContext = applicationContext;
    }

    public static Context getContext() {
        return sContext;
    }

}
