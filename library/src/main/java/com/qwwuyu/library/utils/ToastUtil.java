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
    /**  */
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast;

    private ToastUtil() {
        throw new UnsupportedOperationException("can't instantiate");
    }

    /** 显示Toast */
    public static void showToast(final Context context, final Object text) {
        if (text == null || context == null) return;
        showToast(context, text.toString(), Toast.LENGTH_SHORT);
    }

    /** 显示Toast */
    public static void showToast(final Context context, final int id) {
        if (context == null) return;
        showToast(context, context.getResources().getText(id));
    }

    /** 显示Toast */
    private static void showToast(final Context context, final CharSequence text, final int duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = makeText(context.getApplicationContext(), text, duration);
                } else {
                    toast.setText(text);
                    toast.setDuration(duration);
                }
                toast.show();
            }
        });
    }

    /** 创建一个Toast */
    private static Toast makeText(Context context, CharSequence text, int duration) {
        return Toast.makeText(context, text, duration);
    }
}