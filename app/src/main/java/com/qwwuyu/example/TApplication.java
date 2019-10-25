package com.qwwuyu.example;

import com.qwwuyu.lib.base.BaseApplication;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.DfrUtil;
import com.qwwuyu.lib.utils.InitUtil;
import com.qwwuyu.lib.utils.LogUtils;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by qiwei on 2017/7/13
 */
public class TApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;

        new LogUtils.Builder().enableLogHead(true).setHeadSep(" : ").setLogTag("qwwuyu");
        DfrUtil.getInstance().setEnable(BuildConfig.DEBUG);
        KeepLifecycleCallbacks callbacks = new KeepLifecycleCallbacks() {
            @Override
            protected void wakeOnce() {
            }
        };
        registerActivityLifecycleCallbacks(callbacks);
        registerComponentCallbacks(callbacks);
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

    @Override
    protected void modifyConfig(InitUtil.Configuration config) {

    }
}
