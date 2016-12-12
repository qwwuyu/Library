package com.qwwuyu.library.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 简单的Toast工具
 * Created by qiwei on 2016/12/1
 */
public class ToastUtil {
    /** 主线程Handler */
    private static Handler handler = new Handler(Looper.getMainLooper());
    /** 主线程id */
    private static long threadID = Looper.getMainLooper().getThread().getId();
    /** 懒加载单例Toast */
    private static Toast toast;
    /** ApplicationContext */
    private static Context appContext;

    private ToastUtil() {
        throw new UnsupportedOperationException("can't instantiate");
    }

    public synchronized static void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
        }
    }

    /** 显示Toast */
    public static void showToast(final Object text) {
        check();
        if (text == null) return;
        showToast(text.toString(), Toast.LENGTH_SHORT);
    }

    /** 根据StringID显示Toast */
    public static void showToast(final int id) {
        check();
        showToast(appContext.getResources().getText(id), Toast.LENGTH_SHORT);
    }

    private static void showToast(final CharSequence text, final int duration) {
        if (threadID == Thread.currentThread().getId()) {
            show(text, duration);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    show(text, duration);
                }
            });
        }
    }

    private static void show(final CharSequence text, final int duration) {
        if (toast == null) {
            toast = makeText(appContext, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }

    /** 创建一个Toast */
    private static Toast makeText(Context context, CharSequence text, int duration) {
        return Toast.makeText(context, text, duration);
    }

    private static void check() {
        if (appContext == null) throw new IllegalStateException("ToastUtil must be init before using");
    }
}