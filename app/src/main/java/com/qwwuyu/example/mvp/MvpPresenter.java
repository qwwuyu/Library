package com.qwwuyu.example.mvp;

/**
 * Create by 0222387(qiwei2)
 * On 2018/4/19 18:42
 * Copyright(c) 2017 世联行
 * Description .
 */
public class MvpPresenter extends MvpContract.Presenter{
    @Override
    public void test() {
        mView.test();
    }
}
