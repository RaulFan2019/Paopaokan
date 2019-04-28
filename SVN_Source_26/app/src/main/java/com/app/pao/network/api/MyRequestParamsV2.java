package com.app.pao.network.api;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.db.DBUserEntity;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.http.app.DefaultParamsBuilder;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Raul on 2015/11/12.
 * 网络传输对象
 */
public class MyRequestParamsV2 extends RequestParams {

    //头信息的Key
    private static final String USER_AGENT = "user-agent";


    public MyRequestParamsV2(Context context, String url) {
        super(url);
        //创建头信息
        this.addHeader(USER_AGENT, URLConfig.USER_ANGENT);
        DBUserEntity user = LocalApplication.getInstance().getLoginUser(context);
        if (user != null) {
            this.addBodyParameter("sessionid", user.sessionid);
            this.addBodyParameter("userid", String.valueOf(user.userId));
        } else {
            this.addBodyParameter("sessionid", "14");
            this.addBodyParameter("userid", "14");
        }
//        this.setSslSocketFactory(trustAllSSlSocketFactory);
    }

    private static SSLSocketFactory trustAllSSlSocketFactory;

    public static SSLSocketFactory getTrustAllSSLSocketFactory() {
        if (trustAllSSlSocketFactory == null) {
            synchronized (DefaultParamsBuilder.class) {
                if (trustAllSSlSocketFactory == null) {

                    // 信任所有证书
                    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }};
                    try {
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, trustAllCerts, null);
                        trustAllSSlSocketFactory = sslContext.getSocketFactory();
                    } catch (Throwable ex) {
                        LogUtil.e(ex.getMessage(), ex);
                    }
                }
            }
        }
        return trustAllSSlSocketFactory;
    }


}
