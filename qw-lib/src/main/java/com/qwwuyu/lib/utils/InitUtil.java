package com.qwwuyu.lib.utils;

import android.content.Context;

/**
 * 工具类的初始化
 * Created by qiwei on 2016/12/13
 */
public class InitUtil {
    public static boolean isInit = false;

    private InitUtil() {
        throw new UnsupportedOperationException("can't instantiate");
    }

    /** 初始化工具 */
    public synchronized static void init(Configuration config) {
        if (isInit) return;
        Context context = config.context.getApplicationContext();
        ToastUtil.init(context, config.toastUtil == null ? ToastUtilImpl.getInstance() : config.toastUtil);
        DisplayUtil.init(context);
    }

    public static class Configuration {
        Context context;
        IToastUtil toastUtil;
        public boolean leakCanary = false, blockCanary = false;

        public Configuration(Context context) {
            if (context == null) throw new NullPointerException("context == null");
            this.context = context.getApplicationContext();
        }

        public Configuration setToastUtil(IToastUtil toastUtil) {
            this.toastUtil = toastUtil;
            return this;
        }

        public Configuration setLeakCanary(boolean enable) {
            this.leakCanary = enable;
            return this;
        }

        public Configuration setBlockCanary(boolean enable) {
            this.blockCanary = enable;
            return this;
        }
    }
}
