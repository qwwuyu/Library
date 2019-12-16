package com.qwwuyu.lib.clazz;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import com.qwwuyu.lib.utils.LogUtils;

public abstract class KeepLifecycleCallbacks implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private boolean appInForeground;

    @Override
    public final void onActivityResumed(Activity activity) {
        if (!appInForeground) {
            appInForeground = true;
            wakeOnce();
            LogUtils.i("in Foreground");
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false;
            LogUtils.i("in Background");
        } else if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            appInForeground = false;
            LogUtils.i("in Background2");
        }
    }

    @Override
    public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public final void onActivityStarted(Activity activity) {
    }

    @Override
    public final void onActivityPaused(Activity activity) {
    }

    @Override
    public final void onActivityStopped(Activity activity) {
    }

    @Override
    public final void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public final void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    /**
     * 尝试唤醒一次
     */
    protected abstract void wakeOnce();
}
