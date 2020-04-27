package com.qwwuyu.example;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.qwwuyu.example.activity.ColorActivity;
import com.qwwuyu.example.activity.ELMActivity;
import com.qwwuyu.example.activity.GyroActivity;
import com.qwwuyu.example.activity.TestActivity;
import com.qwwuyu.example.mvp.MvpActivity;
import com.qwwuyu.lib.base.LibMvpActivity;
import com.qwwuyu.lib.base.MvpConfig;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.LogUtils;
import com.qwwuyu.lib.utils.SystemBarUtil;

import androidx.core.content.ContextCompat;

public class MainActivity extends LibMvpActivity {
    private static int[] flag = new int[10];

    @Override
    protected void initMvpConfig(MvpConfig mvpConfig) {
        mvpConfig.layoutResID(R.layout.a_main);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
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
            startActivity(new Intent(this, TestActivity.class));
        } else if (flag[i] % num == ++index) {
            startActivity(new Intent(this, MvpActivity.class));
        }
        flag[i]++;
    }

    public void onClick4(View v) {
        LogUtils.i(BuildConfig.TEST);
        final Snackbar snackbar = Snackbar.make(v, "TestSnackbar", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Close", view -> snackbar.dismiss());
        View snackbarView = snackbar.getView();
        ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLACK);
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }
}