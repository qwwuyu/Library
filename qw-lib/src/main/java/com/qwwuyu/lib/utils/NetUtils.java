package com.qwwuyu.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.qwwuyu.lib.base.BaseApplication;


/**
 * 网络请求工具类
 */
@SuppressLint("MissingPermission")
public class NetUtils {
    private static ConnectivityManager getNetworkManager() {
        return (ConnectivityManager) BaseApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = getNetworkManager();
        if (manager == null) return false;
        NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiAvailable() {
        ConnectivityManager manager = getNetworkManager();
        if (manager == null) return false;
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 检查wifi是否连接
     */
    public static boolean isWifiConnected() {
        ConnectivityManager manager = getNetworkManager();
        if (manager == null) return false;
        NetworkInfo[] info = manager.getAllNetworkInfo();
        if (info == null) return false;
        for (NetworkInfo networkInfo : info) {
            if (networkInfo.getTypeName().equals("WIFI") && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType() {
        ConnectivityManager manager = getNetworkManager();
        if (manager == null) return -1;
        NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
            return mNetworkInfo.getType();
        }
        return -1;
    }
}
