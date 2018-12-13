package cn.cbsd.mvplibrary.net;

/**
 * 当前类注释:
 * Author: zhenyanjun
 * Date  : 2018/1/19 11:16
 */

public class SoapNetConfig {

    private static SoapProvider sProvider = null;

    public static void registerProvider(SoapProvider provider) {
        sProvider = provider;
    }

    public static SoapProvider getProvider() {
        return sProvider;
    }
}
