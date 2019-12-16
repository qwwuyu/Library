package com.qwwuyu.lib.executor;

import android.os.Handler;
import android.os.Looper;

public class MainExecutor {
    private Handler handler = new Handler(Looper.getMainLooper());

    private static class Singleton {
        private static MainExecutor ourInstance = new MainExecutor();
    }

    private MainExecutor() {
    }

    public static MainExecutor getInstance() {
        return Singleton.ourInstance;
    }

    public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}
