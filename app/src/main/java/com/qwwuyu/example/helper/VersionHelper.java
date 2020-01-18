package com.qwwuyu.example.helper;

import android.app.Application;

import com.qwwuyu.example.BuildConfig;
import com.qwwuyu.example.configs.Constant;
import com.qwwuyu.lib.utils.LogUtils;
import com.qwwuyu.lib.utils.SpUtils;


/**
 * Created by qiwei on 2019/9/27.
 * app版本管理
 */
public class VersionHelper {
    /**
     * app版本升级信息管理
     */
    public static void checkUpgrade(Application context) {
        int versionCode = BuildConfig.VERSION_CODE;
        int lastCode = SpUtils.getDefault().getValue(Constant.SP_APP_VERSION, BuildConfig.VERSION_CODE);
        if (versionCode != lastCode) {
            LogUtils.i(null, "lastCode=%s versionCode=%s", lastCode, versionCode);
        }
        SpUtils.getDefault().setValue(Constant.SP_APP_VERSION, BuildConfig.VERSION_CODE);
    }

    /**
     * @return true 第一次请求权限
     */
    public static boolean isFirstRequestPermit() {
        if (SpUtils.getDefault().getValue(Constant.SP_FIRST_PERMIT, true)) {
            SpUtils.getDefault().setValue(Constant.SP_FIRST_PERMIT, false);
            return true;
        } else {
            return false;
        }
    }
}
