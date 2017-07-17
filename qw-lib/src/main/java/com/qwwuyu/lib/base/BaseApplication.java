package com.qwwuyu.lib.base;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.InitUtil;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by qiwei on 2017/7/12
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;
        InitUtil.Configuration config = new InitUtil.Configuration(getApplicationContext());
        modifyConfig(config);
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        if (config.leakCanary) LeakCanary.install(this);
        if (config.blockCanary) BlockCanary.install(this, new AppBlockCanaryContext()).start();
        InitUtil.init(config);
    }

    protected void modifyConfig(InitUtil.Configuration config) {
    }
}
