package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;

import com.google.gson.Gson;
import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.MvpConfig;
import com.qwwuyu.lib.http.body.ProgressResponseBody;
import com.qwwuyu.lib.http.interceptor.HttpLoggingInterceptor;
import com.qwwuyu.lib.base.LibMvpActivity;
import com.qwwuyu.lib.mvp.BasePresenter;
import com.qwwuyu.lib.utils.LogUtils;
import com.qwwuyu.lib.base.MultipleStateLayout;
import com.qwwuyu.lib.widget.PercentProgressBar;
import com.qwwuyu.lib.base.TitleView;
import com.qwwuyu.library.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by qiwei on 2017/8/12
 */
public class HttpActivity extends LibMvpActivity {
    private Api api;
    private Gson gson = new Gson();
    private PercentProgressBar bar;
    private OkHttpClient okHttpClient;
    private String host = "http://192.168.20.42/";
    private String token = "eyJhY2MiOiJxd3d1eXUiLCJuaWNrIjoicXd3dXl1IiwiYXV0aCI6NSwiaWQiOjIsInV1aWQiOiI1ZmM4MTcxNy1hMzI3LTQxN2ItYWQ0NC0zZTBhY2IxMzNhNmQifQ==";

    @Override
    protected void initMvpConfig(MvpConfig mvpConfig) {
        mvpConfig.layoutResID(R.layout.a_http);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bar = findViewById(R.id.progressBar);
        bar.setProgress(0, 100);
        initHttp();
    }

    private static <T> ObservableTransformer<T, T> compose() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void onClick1(View GET) {
        api.get("get", "value").compose(compose()).subscribe(new SObserver<>("get"));
    }

    public void onClick2(View POST) {
        api.post("post", "query", "field").compose(compose()).subscribe(new SObserver<>("post"));
    }

    public void onClick3(View TIMEOUT) {
        api.timeout().compose(compose()).subscribe(new SObserver<>("timeout"));
    }

    public void onClick4(View ERROR) {
        api.error().compose(compose()).subscribe(new SObserver<>("error"));
    }

    public void onClick7(View NOTFOUND) {
        api.notFound().compose(compose()).subscribe(new SObserver<>("notFound"));
    }

    public void onClick5(View UPLOAD) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AAAA/txt/1.txt");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        ProgressResponseBody progressFileBody = new ProgressResponseBody(fileBody, new ProgressResponseBody.MainProgressListener() {
            @Override
            public void onMainProgressUpdate(long read, long length) {
                LogUtils.i("upload", "read:" + read + " length:" + length);
                bar.setProgress((int) (read * 100 / length) / 100);
            }

            @Override
            public void onMainError() {
                LogUtils.i("upload", "onError");
                bar.setProgress(0);
            }

            @Override
            public void onMainFinish(long length) {
                LogUtils.i("upload", "onFinish:" + length);
                bar.setProgress(100);
            }
        });
        MultipartBody.Part picturePart = MultipartBody.Part.createFormData("pName", file.getName(), progressFileBody);
        RequestBody fieldBody = RequestBody.create(MediaType.parse("text/plain"), "fileName");
        api.upload(picturePart, fieldBody, token).compose(compose()).subscribe(new SObserver<>("upload"));
    }

    public void onClick6(View DOWNLOAD) {
        LogUtils.i("download:main" + Looper.getMainLooper().getThread().getId());
//        api.download().enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                LogUtils.i("download:onResponse" + Thread.currentThread().getId());
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                LogUtils.i("download:onFailure" + Thread.currentThread().getId() + t.getMessage());
//            }
//        });
        Request request = new Request.Builder()
                .url(host + "test/download?name=1.txt&token=" + token)
                .build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                LogUtils.i("download:onFailure" + Thread.currentThread().getId() + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) throw new IOException("body is null");
                InputStream is = body.byteStream();
                final long length = body.contentLength();
                byte[] buf = new byte[1024 * 100];
                int len;
                long read = 0;
                while ((len = is.read(buf)) != -1) {
                    read += len;
                    LogUtils.i("download:read:" + read + " length" + length);
                }
                body.close();
                LogUtils.i("download:over");
            }
        });
    }

    private class SObserver<T> implements Observer<T> {
        private String tag;

        public SObserver(String tag) {
            this.tag = tag;
        }

        @Override
        public void onSubscribe(Disposable d) {
            LogUtils.i(tag, "onSubscribe");
        }

        @Override
        public void onNext(T t) {
            LogUtils.i(tag, "onNext:" + gson.toJson(t));
        }

        @Override
        public void onError(Throwable e) {
            LogUtils.i(tag, "onError:" + e.getMessage());
        }

        @Override
        public void onComplete() {
            LogUtils.i(tag, "onComplete");
        }
    }

    private void initHttp() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                .cache(new Cache(new File(getCacheDir().getAbsolutePath() + File.separator + "http"), 30L * 1024 * 1024))
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            okBuilder.addInterceptor(new HttpLoggingInterceptor(message -> LogUtils.i("http", message)).setLevel(HttpLoggingInterceptor.Level.BASIC));
        }
        if (false) {
            okBuilder.retryOnConnectionFailure(true)
                    .sslSocketFactory(null, null)
                    .cookieJar(CookieJar.NO_COOKIES);
        }
        okHttpClient = okBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public interface Api {
        @GET("test/{path}")
        Observable<ResponseBean> get(@Path("path") String path, @Query("key") String value);

        @FormUrlEncoded
        @POST("test/{path}")
        Observable<ResponseBean> post(@Path("path") String path, @Query("query") String query, @Field("field") String field);

        @POST("test/timeout")
        Observable<ResponseBean> timeout();

        @POST("test/error")
        Observable<ResponseBean> error();

        @POST("http://www.qweasdzxc.com/test/get")
        Observable<ResponseBean> notFound();

        @Multipart
        @POST("test/upload")
        Observable<ResponseBean> upload(@Part MultipartBody.Part file, @Part("fileName") RequestBody fileName, @Query("token") String token);
    }

    public class ResponseBean {
        /** 返回状态码 */
        public int statu;
        /** 返回消息 */
        public String info;
        /** 返回数据 */
        public Object data;
    }
}