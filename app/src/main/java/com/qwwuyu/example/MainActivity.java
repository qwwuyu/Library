package com.qwwuyu.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qwwuyu.lib.utils.InitUtil;
import com.qwwuyu.lib.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test).setOnClickListener(this);
        InitUtil.init(new InitUtil.Configuration(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                ToastUtil.show("123");
                break;
            default:
                break;
        }
    }
}