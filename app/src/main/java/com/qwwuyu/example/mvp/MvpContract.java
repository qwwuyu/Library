package com.qwwuyu.example.mvp;

import com.qwwuyu.lib.mvp.BaseView;
import com.qwwuyu.lib.mvp.WrapperPresenter;

/**
 * Create by 0222387(qiwei2)
 * On 2018/4/19 18:24
 * Copyright(c) 2017 世联行
 * Description .
 */
public interface MvpContract {
    interface View extends BaseView {
        void test();
    }

    interface IPresenter {
        void test();
    }

    abstract class Presenter extends WrapperPresenter<View> implements IPresenter {

    }
}
