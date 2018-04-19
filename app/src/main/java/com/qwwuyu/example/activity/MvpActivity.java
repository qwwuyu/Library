package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qwwuyu.example.R;
import com.qwwuyu.example.mvp.MvpContract;
import com.qwwuyu.example.mvp.MvpPresenter;
import com.qwwuyu.lib.mvp.BaseMvpActivity;
import com.qwwuyu.lib.utils.ToastUtil;

/**
 * Create by 0222387(qiwei2)
 * On 2018/4/19 18:22
 * Copyright(c) 2017 世联行
 * Description .
 */
public class MvpActivity extends BaseMvpActivity<MvpContract.Presenter> implements MvpContract.View {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_mvp);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showError(int code, String msg) {

    }

    @Override
    protected MvpContract.Presenter createPresenter() {
        return new MvpPresenter();
    }

    public void onClick1(View view) {
        mPresenter.test();
    }

    @Override
    public void test() {
        ToastUtil.show("mvp");
    }
}
