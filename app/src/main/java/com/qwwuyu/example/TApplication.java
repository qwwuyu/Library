package com.qwwuyu.example;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.qwwuyu.lib.base.BaseApplication;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.DfrUtil;
import com.qwwuyu.lib.utils.InitUtil;
import com.qwwuyu.lib.utils.LogUtil;
import com.qwwuyu.lib.utils.glide.DefaultGlideModule;
import com.qwwuyu.lib.utils.glide.GlideConfig;

/**
 * Created by qiwei on 2017/7/13
 */
public class TApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!CommUtil.isInMainProcess(getApplicationContext())) return;

        DefaultGlideModule.setGlideConfig(new GlideConfig.Builder()
                .options(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)//图片格式
                        .placeholder(0)//加载中图片
                        .error(0)//加载失败图片
                ).build());
        new LogUtil.Builder()
                .enableLogBorder(false)
                .enableLogHead(true)
                .setHeadSep(" : ")
                .setLogTag("qwwuyu");
        DfrUtil.getInstance().setEnable(false);
    }

    @Override
    protected void modifyConfig(InitUtil.Configuration config) {
        config.enableLeakCanary();
    }
}
