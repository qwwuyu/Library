package com.qwwuyu.lib.utils.permit;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by qiwei on 2017/8/4
 * 简单实现
 */
public abstract class SimplePermitCtrl implements PermitCtrl {
    @Override
    public boolean beforeRequest(List<String> showRationales, @NonNull List<String> request, ProceedCallback proceedListener) {
        return false;
    }

    @Override
    public void onGranted() {
    }

    @Override
    public void onDenied(@NonNull List<String> granted, @NonNull List<String> onlyDenied, @NonNull List<String> foreverDenied) {
    }
}
