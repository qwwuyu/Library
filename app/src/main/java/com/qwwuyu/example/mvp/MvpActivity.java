package com.qwwuyu.example.mvp;

import android.os.Bundle;
import android.view.View;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.LibMvpActivity;
import com.qwwuyu.lib.base.MvpConfig;
import com.qwwuyu.lib.utils.ToastUtil;

/**
 * Created by qiwei on 2018/6/14 14:02
 * Description .
 */
public class MvpActivity extends LibMvpActivity<MvpContract.Presenter> implements MvpContract.View {
    @Override
    protected void initMvpConfig(MvpConfig<MvpContract.Presenter> mvpConfig) {
        mvpConfig.presenter(new MvpPresenter(this))
                .layoutResID(R.layout.a_mvp)
                .useTitle(titleView -> titleView.setTitle("返回"))
                .useState(stateLayout -> stateLayout.setAllListener(v -> showMsg(0, "state click")));
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    public void onClick1(android.view.View view) {
        presenter.test();
    }

    @Override
    public void test() {
        ToastUtil.show("mvp");
    }
}
