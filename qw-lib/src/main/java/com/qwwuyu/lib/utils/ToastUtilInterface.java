package com.qwwuyu.lib.utils;

import android.content.Context;

/**
 * Created by qiwei on 2016/12/13
 */
public interface ToastUtilInterface {
    /** 初始化工具,这个方法必须其他方法调用前使用 */
    void init(Context context);

    /**
     * @param text 将要显示的对象,默认显示的时间{@link android.widget.Toast#LENGTH_SHORT}
     */
    void show(Object text);

    /**
     * @param id 将要显示的String资源id,默认显示的时间{@link android.widget.Toast#LENGTH_SHORT}
     */
    void show(int id);

    /**
     * @param text     将要显示的对象
     * @param duration 显示的时间{@link android.widget.Toast#LENGTH_SHORT}{@link android.widget.Toast#LENGTH_LONG}
     */
    void show(Object text, int duration);

    /**
     * @param id       将要显示的String资源id
     * @param duration 显示的时间{@link android.widget.Toast#LENGTH_SHORT}{@link android.widget.Toast#LENGTH_LONG}
     */
    void show(int id, int duration);
}
