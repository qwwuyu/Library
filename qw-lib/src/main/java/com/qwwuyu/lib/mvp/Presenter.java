package com.qwwuyu.lib.mvp;

/**
 * Created by qiwei on 2018/4/19 17:38
 * Description .
 */
public interface Presenter<V extends BaseView> {
    void attachView(V view);

    void detachView();
}