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
 * Created by qiwei on 2018/6/14 14:02
 * Description .
 */
public class MvpActivity extends BaseMvpActivity<MvpContract.Presenter> implements MvpContract.View {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_mvp);
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
