package com.qwwuyu.lib.base;

import android.app.Application;

import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.InitUtil;

/**
 * Created by qiwei on 2017/7/12
 */
public abstract class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;
        InitUtil.Configuration config = new InitUtil.Configuration(getApplicationContext());
        modifyConfig(config);
        InitUtil.init(config);
    }

    protected abstract void modifyConfig(InitUtil.Configuration config);
}
