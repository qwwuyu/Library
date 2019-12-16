package com.qwwuyu.lib.base;

import android.app.Application;
import android.content.Context;

import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.DisplayUtils;
import com.qwwuyu.lib.utils.LogUtils;

/**
 * Created by qiwei on 2017/7/12
 */
public abstract class BaseApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;
        DisplayUtils.init(this);
        LogUtils.i("BaseApplication", "onCreate InMainProcess");
    }
}
