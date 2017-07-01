package com.qwwuyu.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qwwuyu.lib.utils.InitUtil;
import com.qwwuyu.lib.utils.SystemBarUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitUtil.init(new InitUtil.Configuration(this));
    }

    public void onClick1(View v) {
        SystemBarUtil.setStatusBarColor(this, 0xffff0000);
    }

    public void onClick2(View v) {
        SystemBarUtil.translucentStatusBar(this, true);
    }

    public void onClick3(View v) {
        SystemBarUtil.translucentStatusBar(this, false);
    }
}