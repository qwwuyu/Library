package com.qwwuyu.example;

import android.os.Environment;

import com.qwwuyu.lib.base.BaseApplication;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.InitUtil;
import com.qwwuyu.lib.utils.LogUtil;
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
        LogUtil.i("file:" + Environment.getRootDirectory());
    }
    
    @Override
    protected void modifyConfig(InitUtil.Configuration config) {
        config.enableLeakCanary();
    }
}
