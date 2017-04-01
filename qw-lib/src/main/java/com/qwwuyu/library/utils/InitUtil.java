package com.qwwuyu.library.utils;

import android.content.Context;

/**
 * 工具类的初始化
 * Created by qiwei on 2016/12/13
 */
public class InitUtil {
    public static boolean isInit = false;

    /** 初始化工具 */
    public static void init(Configuration config) {
        if (isInit) return;
        Context context = config.context;
        ToastUtil.init(context, config.toastUtil == null ? ToastUtilImpl.getInstance() : config.toastUtil);
    }

    public static class Configuration {
        Context context;
        IToastUtil toastUtil;

        public Configuration(Context context) {
            if (context == null) throw new NullPointerException("context == null");
            this.context = context.getApplicationContext();
        }

        public Configuration toastUtil(IToastUtil toastUtil) {
            this.toastUtil = toastUtil;
            return this;
        }
    }
}
