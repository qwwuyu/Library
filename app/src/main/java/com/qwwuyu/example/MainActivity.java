package com.qwwuyu.example;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qwwuyu.lib.utils.SystemBarUtil;
import com.qwwuyu.lib.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick1(View v) {
        SystemBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    public void onClick2(View v) {
        SystemBarUtil.translucentStatusBar(this, true);
    }

    public void onClick3(View v) {
        SystemBarUtil.translucentStatusBar(this, false);
    }

    public void onClick4(View v) {
        ToastUtil.show("123");
    }
}