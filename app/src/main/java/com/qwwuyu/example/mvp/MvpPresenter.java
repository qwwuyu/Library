package com.qwwuyu.example.mvp;

import com.qwwuyu.lib.mvp.RxBaseModel;

/**
 * Created by qiwei on 2018/6/14 14:01
 * Description .
 */
public class MvpPresenter extends MvpContract.Presenter<RxBaseModel> {
    public MvpPresenter(MvpContract.View view) {
        super(view, new RxBaseModel());
    }

    @Override
    public void test() {
        view.test();
    }
}
