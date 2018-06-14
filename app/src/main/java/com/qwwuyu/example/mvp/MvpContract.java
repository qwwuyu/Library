package com.qwwuyu.example.mvp;

import com.qwwuyu.lib.mvp.BaseView;
import com.qwwuyu.lib.mvp.WrapperPresenter;

/**
 * Created by qiwei on 2018/6/14 14:02
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
