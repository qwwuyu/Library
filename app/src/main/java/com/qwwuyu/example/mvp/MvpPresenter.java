package com.qwwuyu.example.mvp;

/**
 * Created by qiwei on 2018/6/14 14:01
 * Description .
 */
public class MvpPresenter extends MvpContract.Presenter{
    @Override
    public void test() {
        mView.test();
    }
}
