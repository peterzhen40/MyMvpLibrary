package cn.cbsd.base.net.kit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by liuchongming on 2017/6/5.
 * 主机名验证
 */

public class UnSafeHostnameVerifier implements
        HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
//        return verify("gzyzd.cn", session);
        return true;
    }
}
