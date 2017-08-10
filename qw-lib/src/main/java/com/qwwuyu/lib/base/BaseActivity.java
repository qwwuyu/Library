package com.qwwuyu.lib.base;

import android.content.Context;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by qiwei on 2017/7/13
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    protected Context context = BaseActivity.this;
}
