package com.qwwuyu.library.utils;

import android.content.pm.PackageManager;

/**
 * 通用的工具类
 * Created by qiwei on 2016/12/1
 */
public class CommUtil {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);//GET_UNINSTALLED_PACKAGES
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
