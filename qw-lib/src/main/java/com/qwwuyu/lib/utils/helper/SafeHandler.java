package com.qwwuyu.lib.utils.helper;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by qiwei on 2017/8/3
 */
public class SafeHandler extends Handler {
    public interface Callback {
        void handleMessage(Message msg);
    }

    private WeakReference<Callback> wr;

    public SafeHandler(Callback t) {
        wr = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        Callback t = wr.get();
        if (t != null) t.handleMessage(msg);
    }

    public void onDestroy() {
        wr.clear();
        removeCallbacksAndMessages(null);
    }
}