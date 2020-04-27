package com.qwwuyu.lib.base;

import com.qwwuyu.lib.mvp.BasePresenter;

/**
 * Mvp配置
 */
public class MvpConfig<P extends BasePresenter> {
    P presenter;
    int layoutResID;
    TitleCallBack titleCallBack;
    StateCallBack stateCallBack;
    LazyCallBack lazyCallBack;

    public MvpConfig presenter(P presenter) {
        this.presenter = presenter;
        return this;
    }

    public MvpConfig layoutResID(int layoutResID) {
        this.layoutResID = layoutResID;
        return this;
    }

    public MvpConfig useTitle(TitleCallBack titleCallBack) {
        this.titleCallBack = titleCallBack;
        return this;
    }

    public MvpConfig useState(StateCallBack stateCallBack) {
        this.stateCallBack = stateCallBack;
        return this;
    }

    public MvpConfig useLazy(LazyCallBack lazyCallBack) {
        this.lazyCallBack = lazyCallBack;
        return this;
    }

    public interface TitleCallBack {
        void call(TitleView titleView);
    }

    public interface StateCallBack {
        void call(MultipleStateLayout stateLayout);
    }

    public interface LazyCallBack {
        void call();
    }
}
