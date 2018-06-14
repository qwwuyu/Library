package com.qwwuyu.lib.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qwwuyu.lib.base.BaseActivity;

/**
 * Created by qiwei on 2018/4/19 17:48
 * Description .
 */
public abstract class BaseMvpActivity<P extends WrapperPresenter> extends BaseActivity implements BaseView {
    protected Context context = BaseMvpActivity.this;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    private void initPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }

    protected abstract P createPresenter();
}
