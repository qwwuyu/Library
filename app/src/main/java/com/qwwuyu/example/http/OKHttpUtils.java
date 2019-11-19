package com.qwwuyu.example.http;


import com.qwwuyu.example.BuildConfig;
import com.qwwuyu.lib.http.interceptor.HttpLoggingInterceptor;
import com.qwwuyu.lib.utils.LogUtils;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

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
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager));
        //HttpsFactory.safeHttps(builder);
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
}
