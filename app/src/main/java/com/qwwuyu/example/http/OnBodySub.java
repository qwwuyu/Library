package com.qwwuyu.example.http;

import android.content.Context;
import android.content.DialogInterface;

import com.qwwuyu.example.configs.Constant;
import com.qwwuyu.lib.utils.LogUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 传递context默认弹dialog,根据cancelRequest取消弹窗并取消请求.
 */
public abstract class OnBodySub extends OnBaseSub implements DialogInterface.OnDismissListener {
    public OnBodySub() {
    }

    public OnBodySub(Context context) {
        super(context);
    }

    public OnBodySub(Context context, boolean cancelRequest) {
        super(context, cancelRequest);
    }

    @Override
    public void onNext(ResponseBody body) {
        try {
            onSuccess(body.string());
        } catch (IOException e) {
            onFault(Constant.HTTP_NO_CODE, "获取数据失败");
        } catch (Exception e) {
            onFault(Constant.HTTP_DATA_ERR, "数据处理出错");
        }
    }

    @Override
    public void onError1(Throwable e) {
        LogUtils.e("error:" + e.getMessage());
        if (e instanceof SocketTimeoutException) {//请求超时
            onFault(Constant.HTTP_NO_CODE, "网络连接超时");
        } else if (e instanceof ConnectException) {//网络连接失败
            onFault(Constant.HTTP_NO_CODE, "网络连接失败");
        } else if (e instanceof SSLHandshakeException) {//安全证书异常
            onFault(Constant.HTTP_NO_CODE, "安全证书异常");
        } else if (e instanceof HttpException) {//请求的地址不存在
            int code = ((HttpException) e).code();
            if (code == 504) {
                onFault(Constant.HTTP_NO_CODE, "服务器连接超时");
            } else if (code == 404) {
                onFault(Constant.HTTP_NO_CODE, "请求数据失败");
            } else {
                onFault(Constant.HTTP_NO_CODE, "请求数据失败");
            }
        } else if (e instanceof UnknownHostException) {//域名解析失败
            onFault(Constant.HTTP_NO_CODE, "请求服务器失败");
        } else if (e instanceof NoRouteToHostException) {//域名解析失败
            onFault(Constant.HTTP_NO_CODE, "请求服务器失败");
        } else {
            onFault(Constant.HTTP_NO_MSG, "error:" + e.getMessage());
        }
    }

    public abstract void onSuccess(String result);

    public abstract void onFault(int code, String errorMsg);
}
