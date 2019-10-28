package com.qwwuyu.example.http;


import android.content.Context;

import com.qwwuyu.example.BuildConfig;
import com.qwwuyu.example.WApplication;
import com.qwwuyu.lib.http.interceptor.HttpLoggingInterceptor;
import com.qwwuyu.lib.utils.LogUtils;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OKHttpUtils {
    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static OKHttpUtils instance;
    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;
    private final HttpApi api;

    private OKHttpUtils() {
        Context context = WApplication.context;

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(), trustManager)
                .hostnameVerifier((hostname, session) -> true)
                .cookieJar(new JavaNetCookieJar(cookieManager));
        if (LogUtils.LOG) {//打印请求日志
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> LogUtils.log(LogUtils.D, false, "okhttp", 2, message));
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        OkHttpClient client = builder.build();
        okHttpClient = client;

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(HttpApi.class);
    }

    public static OKHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OKHttpUtils.class) {
                if (instance == null) {
                    instance = new OKHttpUtils();
                }
            }
        }
        return instance;
    }

    public static HttpApi getApi() {
        return getInstance().api;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    private SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    final X509TrustManager trustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
}
