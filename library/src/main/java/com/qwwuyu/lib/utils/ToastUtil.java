package com.qwwuyu.lib.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.qwwuyu.lib.base.BaseApplication;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
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

    private static Context getContext() {
        return BaseApplication.context;
    }

    public static void show(Object text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int id) {
        show(id, Toast.LENGTH_SHORT);
    }

    public static void show(Object text, int duration) {
        if (text == null || text.toString().length() == 0) return;
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
        if (toast == null) {
            toast = makeText(getContext(), text, duration);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    Field field = View.class.getDeclaredField("mContext");
                    field.setAccessible(true);
                    field.set(toast.getView(), new ApplicationContextWrapperForApi25(getContext()));
                } catch (Throwable ignored) {
                }
            }
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

    private static final class ApplicationContextWrapperForApi25 extends ContextWrapper {
        ApplicationContextWrapperForApi25(Context context) {
            super(context.getApplicationContext());
        }

        @Override
        public Context getApplicationContext() {
            return this;
        }

        @Override
        public Object getSystemService(@NonNull String name) {
            if (Context.WINDOW_SERVICE.equals(name)) {
                return new WindowManagerWrapper((WindowManager) getBaseContext().getSystemService(name));
            }
            return super.getSystemService(name);
        }

        private static final class WindowManagerWrapper implements WindowManager {
            private final WindowManager base;

            private WindowManagerWrapper(@NonNull WindowManager base) {
                this.base = base;
            }

            @Override
            public Display getDefaultDisplay() {
                return base.getDefaultDisplay();
            }

            @Override
            public void removeViewImmediate(View view) {
                base.removeViewImmediate(view);
            }

            @Override
            public void addView(View view, ViewGroup.LayoutParams params) {
                try {
                    base.addView(view, params);
                } catch (Throwable ignored) {
                }
            }

            @Override
            public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
                base.updateViewLayout(view, params);
            }

            @Override
            public void removeView(View view) {
                base.removeView(view);
            }
        }
    }
}