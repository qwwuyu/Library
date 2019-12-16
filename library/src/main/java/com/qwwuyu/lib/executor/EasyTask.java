package com.qwwuyu.lib.executor;

/**
 * 简单的异步处理
 */
public abstract class EasyTask<T> implements Runnable {
    public EasyTask() {
    }

    @Override
    public void run() {
        final T t = doInBack();
        MainExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                onPost(t);
            }
        });
    }

    public abstract T doInBack();

    public abstract void onPost(T data);
}