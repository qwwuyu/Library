package com.qwwuyu.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.qwwuyu.example.activity.ColorActivity;
import com.qwwuyu.example.activity.ELMActivity;
import com.qwwuyu.lib.base.BaseActivity;
import com.qwwuyu.lib.utils.LogUtil;
import com.qwwuyu.lib.utils.SystemBarUtil;
import com.qwwuyu.lib.utils.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;

public class MainActivity extends BaseActivity {
    private static int[] flag = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
    }

    public void onClick1(View v) {
        Intent intent = new Intent(this, ColorActivity.class);
        startActivity(intent);
    }

    public void onClick2(View v) {
        if (flag[0] % 3 == 0) SystemBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
        else if (flag[0] % 3 == 1) SystemBarUtil.translucentStatusBar(this, false);
        else if (flag[0] % 3 == 2) SystemBarUtil.translucentStatusBar(this, true);
        flag[0]++;
    }

    public void onClick3(View v) {
        Intent intent = new Intent(this, ELMActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick4(View v) {
        ToastUtil.show("123");
    }

    public void onClick5(View v) {
        LogUtil.i(null);
    }

    public void onClick6(View v) {
        try {
            CrashReport.setUserSceneTag(this, 9527);
            CrashReport.setUserId("9527");
            CrashReport.testJavaCrash();
        } catch (Throwable thr) {
            CrashReport.postCatchedException(thr);
        }
    }
}