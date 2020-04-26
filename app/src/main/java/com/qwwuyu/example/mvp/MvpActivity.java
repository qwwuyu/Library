package com.qwwuyu.example.mvp;

import android.os.Bundle;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.LibMvpActivity;
import com.qwwuyu.lib.utils.ToastUtil;
import com.qwwuyu.lib.base.MultipleStateLayout;
import com.qwwuyu.lib.base.TitleView;

/**
 * Created by qiwei on 2018/6/14 14:02
 * Description .
 */
public class MvpActivity extends LibMvpActivity<MvpContract.Presenter> implements MvpContract.View {

    @Override
    protected int getContentLayout() {
        return R.layout.a_mvp;
    }

    @Override
    protected MvpContract.Presenter createPresenter() {
        return new MvpPresenter(this);
    }

    @Override
    protected void init(Bundle bundle, TitleView titleView, MultipleStateLayout stateLayout) {

    }

    public void onClick1(android.view.View view) {
        presenter.test();
    }

    @Override
    public void test() {
        ToastUtil.show("mvp");
    }
}
