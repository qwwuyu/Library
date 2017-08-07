package com.qwwuyu.lib.clazz;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;

/**
 * Created by qiwei on 2017/8/3
 */
public class SafeHandler extends Handler {
    private SoftReference<com.qwwuyu.lib.clazz.Callback> sf;

    public SafeHandler(com.qwwuyu.lib.clazz.Callback t) {
        sf = new SoftReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        com.qwwuyu.lib.clazz.Callback t = sf.get();
        if (t != null) t.handleMessage(msg);
    }
}