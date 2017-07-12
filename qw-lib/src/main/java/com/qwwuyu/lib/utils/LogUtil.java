package com.qwwuyu.lib.utils;

import android.util.Log;

import com.qwwuyu.library.BuildConfig;

/**
 * Created by qiwei on 2017/7/12
 */
public class LogUtil {
    private static boolean IS_LOG = BuildConfig.DEBUG;
    private static String DEFAULT_TAG = "qw_tag";

    public static void setConfig(boolean enable) {
        IS_LOG = enable;
    }

    public static void setConfig(boolean enable, String defaultTag) {
        IS_LOG = enable;
        DEFAULT_TAG = defaultTag;
    }

    public static void i(String tag, Object obj) {
        if (IS_LOG && null != obj) {
            Log.i(tag, obj.toString());
        }
    }

    public static void i(Object obj) {
        if (IS_LOG && null != obj) {
            Log.i(DEFAULT_TAG, obj.toString());
        }
    }
}
