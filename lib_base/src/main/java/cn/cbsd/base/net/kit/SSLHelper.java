package cn.cbsd.base.net.kit;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 当前类注释:https验证工具（单向验证）
 * @author zhenyanjun
 * @date   2018/12/18 09:32
 */

public class SSLHelper {

    /**
     * 证书类型
     */
    private static final String KEY_STORE_TYPE_BKS = "bks";

    /**
     * truststore文件
     * 记得添加相应的证书到assets目录下面
     */
    private static final String KEY_STORE_TRUST_PATH = "client.truststore";

    /**
     * truststore文件密码
     */
    private static final String KEY_STORE_TRUST_PASSWORD = "sljahch";

    private static final String KEY_CRT_CLIENT_PATH = "server.crt";

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        SSLSocketFactory factory = null;

        try {
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

            InputStream tsIn = context.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
            try {
                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    tsIn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //信任管理器
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            factory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | IOException e) {
            e.printStackTrace();
        }

        return factory;
    }

    public static X509TrustManager getTrustManager(Context context) {
        X509TrustManager trustManager = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = context.getResources().getAssets().open(KEY_CRT_CLIENT_PATH);
            final Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            trustManager =  new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //校验客户端证书
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //校验服务器证书
                    for (X509Certificate cert : chain) {
                        cert.checkValidity();
                        try {
                            cert.verify(ca.getPublicKey());
                        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trustManager;
    }
}
