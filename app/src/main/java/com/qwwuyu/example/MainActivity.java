package com.qwwuyu.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qwwuyu.example.activity.ColorActivity;
import com.qwwuyu.example.activity.ELMActivity;
import com.qwwuyu.example.activity.GyroActivity;
import com.qwwuyu.example.activity.HttpActivity;
import com.qwwuyu.example.mvp.MvpActivity;
import com.qwwuyu.lib.base.BaseMvpActivity;
import com.qwwuyu.lib.mvp.BasePresenter;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.LogUtils;
import com.qwwuyu.lib.utils.SystemBarUtil;
import com.qwwuyu.lib.widget.MultipleStateLayout;
import com.qwwuyu.lib.widget.TitleView;

import androidx.core.content.ContextCompat;

public class MainActivity extends BaseMvpActivity {
    private static int[] flag = new int[10];

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.a_main;
    }

    @Override
    protected void init(Bundle bundle, TitleView titleView, MultipleStateLayout stateLayout) {
        TextView tv = findViewById(R.id.tv);
        tv.setText(CommUtil.getVersionName(context));
    }

    public void onClick1(View v) {
        int i = 0, num = 3, index = -1;
        if (flag[i] % num == ++index) {
            startActivity(new Intent(this, ELMActivity.class));
            finish();
        } else if (flag[i] % num == ++index) {
            startActivity(new Intent(this, ColorActivity.class));
        } else if (flag[i] % num == ++index) {
            startActivity(new Intent(this, GyroActivity.class));
        }
        flag[i]++;
    }

    public void onClick2(View v) {
        int i = 1, num = 3, index = -1;
        if (flag[i] % num == ++index)
            SystemBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
        else if (flag[i] % num == ++index) SystemBarUtil.translucentStatusBar(this, false);
        else if (flag[i] % num == ++index) SystemBarUtil.translucentStatusBar(this, true);
        flag[i]++;
    }

    public void onClick3(View v) {
        int i = 2, num = 3, index = -1;
        if (flag[i] % num == ++index) {
            startActivity(new Intent(this, HttpActivity.class));
        } else if (flag[i] % num == ++index) {
            startActivity(new Intent(this, MvpActivity.class));
        }
        flag[i]++;
    }

    public void onClick4(View v) {
        LogUtils.i(BuildConfig.TEST);
    }

    class AJ8 implements J8 {
    }

    interface J8 {
        default boolean s() {
            return true;
        }
    }
}