package com.qwwuyu.example.http;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {
    @GET("test/get")
    Observable<ResponseBody> getTest(@Query(value = "abc") String abc);

    @GET("{path}")
    Observable<ResponseBody> getPath(@Path(value = "path", encoded = true) String path);
}
