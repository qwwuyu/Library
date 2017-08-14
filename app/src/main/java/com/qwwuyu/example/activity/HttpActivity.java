package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.BaseActivity;
import com.qwwuyu.lib.http.body.ProgressResponseBody;
import com.qwwuyu.lib.http.interceptor.HttpLoggingInterceptor;
import com.qwwuyu.lib.utils.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
public class HttpActivity extends BaseActivity {
    private Api api;
    private Gson gson = new Gson();
    private NumberProgressBar bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_http);
        bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        bar.setMax(100);
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
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BackHome/1.jpg");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        ProgressResponseBody progressFileBody = new ProgressResponseBody(fileBody, new ProgressResponseBody.MainProgressListener() {
            @Override
            public void onMainProgressUpdate(long read, long length) {
                LogUtil.i("upload", "read:" + read + " length:" + length);
                bar.setProgress((int) (read * 100 / length) / 100);
            }

            @Override
            public void onMainError() {
                LogUtil.i("upload", "onError");
                bar.setProgress(0);
            }

            @Override
            public void onMainFinish(long length) {
                LogUtil.i("upload", "onFinish:" + length);
                bar.setProgress(100);
            }
        });
        MultipartBody.Part picturePart = MultipartBody.Part.createFormData("pName", file.getName(), progressFileBody);
        RequestBody fieldBody = RequestBody.create(MediaType.parse("text/plain"), "pictureName");
        api.upload(picturePart, fieldBody).compose(compose()).subscribe(new SObserver<>("upload"));
    }

    public void onClick6(View DOWNLOAD) {

    }

    private class SObserver<T> implements Observer<T> {
        private String tag;

        public SObserver(String tag) {
            this.tag = tag;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            LogUtil.i(tag, "onSubscribe");
        }

        @Override
        public void onNext(@NonNull T t) {
            LogUtil.i(tag, "onNext:" + gson.toJson(t));
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtil.i(tag, "onError:" + e.getMessage());
        }

        @Override
        public void onComplete() {
            LogUtil.i(tag, "onComplete");
        }
    }

    private void initHttp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> LogUtil.i("http", message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File(getCacheDir().getAbsolutePath() + File.separator + "http"), 30L * 1024 * 1024))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.218/")
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

        @POST("http://192.168.1.234/test/get")
        Observable<ResponseBean> notFound();

        @Multipart
        @POST("test/upload")
        Observable<ResponseBean> upload(@Part MultipartBody.Part file, @Part("fileName") RequestBody fileName);
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