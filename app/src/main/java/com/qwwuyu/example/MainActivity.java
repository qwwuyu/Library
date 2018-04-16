package com.qwwuyu.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.qwwuyu.example.activity.ColorActivity;
import com.qwwuyu.example.activity.ELMActivity;
import com.qwwuyu.example.activity.GyroActivity;
import com.qwwuyu.example.activity.HttpActivity;
import com.qwwuyu.lib.base.BaseActivity;
import com.qwwuyu.lib.utils.LogUtil;
import com.qwwuyu.lib.utils.SystemBarUtil;
import com.qwwuyu.lib.utils.ToastUtil;

public class MainActivity extends BaseActivity {
    private static int[] flag = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
    }

    public void onClick1(View v) {
        int i = 0, num = 3, index = -1;
        if (flag[i] % num == ++index) {
            Intent intent = new Intent(this, ELMActivity.class);
            startActivity(intent);
            finish();
        } else if (flag[i] % num == ++index) {
            Intent intent = new Intent(this, ColorActivity.class);
            startActivity(intent);
        } else if (flag[i] % num == ++index) {
            Intent intent = new Intent(this, GyroActivity.class);
            startActivity(intent);
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

    public void onClick4(View v) {
        ToastUtil.show("123");
    }

    public void onClick5(View v) {
        LogUtil.i(BuildConfig.TEST);
    }

    public void onClick6(View v) {
        Intent intent = new Intent(this, HttpActivity.class);
        startActivity(intent);
    }

    public void onClick7(View v) {
        ToastUtil.show(new AJ8().s());
    }

    class AJ8 implements J8 {
    }

    interface J8 {
        default boolean s() {
            return true;
        }
    }
}