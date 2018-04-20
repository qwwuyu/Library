package com.qwwuyu.lib.base;

import android.content.Context;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by qiwei on 2017/7/13
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    protected Context mContext = this;

    public void showLoading() {

    }

    public void hideLoading() {

    }

    public void showEmpty() {

    }

    public void showError(int code, String msg) {

    }
}
