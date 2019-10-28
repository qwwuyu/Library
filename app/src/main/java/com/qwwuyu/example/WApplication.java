package com.qwwuyu.example;

import android.content.Context;

import com.qwwuyu.lib.base.BaseApplication;
import com.qwwuyu.lib.helper.DfrHelper;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.LogUtils;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

/**
 * Created by qiwei on 2017/7/13
 */
public class WApplication extends BaseApplication {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;

        new LogUtils.Builder().enableLogHead(true).setHeadSep(" : ").setLogTag("qwwuyu");
        DfrHelper.getInstance().setEnable(BuildConfig.DEBUG);
        KeepLifecycleCallbacks callbacks = new KeepLifecycleCallbacks() {
            @Override
            protected void wakeOnce() {
            }
        };
        registerActivityLifecycleCallbacks(callbacks);
        registerComponentCallbacks(callbacks);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new MyObserver());
    }

    public class MyObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onAppBackgrounded() {
            LogUtils.i("onAppBackgrounded");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onAppForegrounded() {
            LogUtils.i("onAppForegrounded");
        }
    }
}
