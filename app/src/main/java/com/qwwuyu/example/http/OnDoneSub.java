package com.qwwuyu.example.http;

import android.content.Context;
import android.content.DialogInterface;

import okhttp3.ResponseBody;

/**
 * 不做数据处理
 */
public abstract class OnDoneSub extends OnBaseSub implements DialogInterface.OnDismissListener {
    public OnDoneSub() {
    }

    public OnDoneSub(Context context) {
        super(context);
    }

    public OnDoneSub(Context context, boolean cancelRequest) {
        super(context, cancelRequest);
    }

    @Override
    public void onNext(ResponseBody body) {
    }

    @Override
    void onError1(Throwable e) {
        onDone();
    }

    @Override
    protected void onComplete1() {
        onDone();
    }

    public abstract void onDone();
}
