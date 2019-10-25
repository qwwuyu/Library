package com.qwwuyu.lib.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * 简单的Toast工具
 * Created by qiwei on 2016/12/1
 */
public final class ToastUtil {
    /** 主线程Handler */
    private static Handler handler = new Handler(Looper.getMainLooper());
    /** 懒加载单例Toast */
    private static Toast toast;
    /** ApplicationContext */
    private static Context appContext;

    public static void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
        }
    }

    private static Context getContext() {
        return appContext;
    }

    public static void show(Object text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int id) {
        show(id, Toast.LENGTH_SHORT);
    }

    public static void show(Object text, int duration) {
        if (text == null) return;
        showToast(text.toString(), duration);
    }

    public static void show(@StringRes int id, int duration) {
        showToast(getContext().getResources().getText(id), duration);
    }

    private static void showToast(final CharSequence text, final int duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(text, duration);
        } else {
            handler.post(() -> show(text, duration));
        }
    }

    private static void show(final CharSequence text, final int duration) {
        try {
            if (toast == null) {
                toast = makeText(getContext(), text, duration);
            } else {
                toast.setText(text);
                toast.setDuration(duration);
            }
            toast.show();
        } catch (Exception ignored) {
        }
    }

    /** 创建一个Toast */
    private static Toast makeText(Context context, CharSequence text, int duration) {
        return Toast.makeText(context, text, duration);
    }
}