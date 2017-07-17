package com.qwwuyu.example;

import com.qwwuyu.lib.base.BaseApplication;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.InitUtil;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by qiwei on 2017/7/13
 */
public class TApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;
        CrashReport.initCrashReport(getApplicationContext(), "99095bbe1c", false);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);
    }

    @Override
    protected void modifyConfig(InitUtil.Configuration config) {
        config.setLeakCanary(true).setBlockCanary(true);
    }
}
