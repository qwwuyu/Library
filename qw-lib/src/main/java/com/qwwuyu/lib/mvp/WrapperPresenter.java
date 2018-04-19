package com.qwwuyu.lib.mvp;

/**
 * Created by qiwei on 2018/4/19 17:39
 * Description .
 */
public abstract class WrapperPresenter<V extends BaseView> implements Presenter<V> {
    protected V mView;

    @Override
    public void attachView(V view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }
}