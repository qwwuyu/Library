package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.BaseActivity;

/**
 * Created by qiwei on 2017/8/10
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test);
    }

    public void onClick1(View view) {
    }
}