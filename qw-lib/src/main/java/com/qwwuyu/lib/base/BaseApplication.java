package com.qwwuyu.lib.base;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.qwwuyu.lib.utils.InitUtil;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by qiwei on 2017/7/12
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        LeakCanary.install(this);
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        InitUtil.Configuration config = new InitUtil.Configuration(getApplicationContext());
        modifyConfig(config);
        InitUtil.init(config);
    }

    protected void modifyConfig(InitUtil.Configuration config) {
    }
}
