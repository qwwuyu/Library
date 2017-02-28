package com.qwwuyu.library.utils;

import android.content.Context;

/**
 * 简单的Toast工具
 * Created by qiwei on 2016/12/1
 */
public final class ToastUtil {
    private static IToastUtil toastUtil;

    private ToastUtil() {
        throw new UnsupportedOperationException("can't instantiate");
    }

    synchronized static void init(Context context, IToastUtil iToastUtil) {
        if (toastUtil != null) return;
        toastUtil = iToastUtil;
        toastUtil.init(context);
    }

    /**
     * @param text 将要显示的对象,默认显示的时间{@link android.widget.Toast#LENGTH_SHORT}
     */
    public static void show(Object text) {
        check();
        toastUtil.show(text);
    }

    /**
     * @param id 将要显示的String资源id,默认显示的时间{@link android.widget.Toast#LENGTH_SHORT}
     */
    public static void show(int id) {
        check();
        toastUtil.show(id);
    }

    /**
     * @param text     将要显示的对象
     * @param duration 显示的时间{@link android.widget.Toast#LENGTH_SHORT}{@link android.widget.Toast#LENGTH_LONG}
     */
    public static void show(Object text, int duration) {
        check();
        toastUtil.show(text, duration);
    }

    /**
     * @param id       将要显示的String资源id
     * @param duration 显示的时间{@link android.widget.Toast#LENGTH_SHORT}{@link android.widget.Toast#LENGTH_LONG}
     */
    public static void show(int id, int duration) {
        check();
        toastUtil.show(id, duration);
    }

    private static void check() {
        if (toastUtil == null) throw new IllegalStateException("ToastUtil must be init before using");
    }
}