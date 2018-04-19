package com.qwwuyu.lib.mvp;

/**
 * Created by qiwei on 2018/4/19 17:17
 * Description .
 */
public interface BaseView {
    void showLoading();

    void hideLoading();

    void showEmpty();

    void showError(int code, String msg);
}
