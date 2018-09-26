package com.qwwuyu.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.qwwuyu.example.activity.ColorActivity;
import com.qwwuyu.example.activity.ELMActivity;
import com.qwwuyu.example.activity.GyroActivity;
import com.qwwuyu.example.activity.HttpActivity;
import com.qwwuyu.example.activity.MvpActivity;
import com.qwwuyu.lib.base.BaseActivity;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.LogUtils;
import com.qwwuyu.lib.utils.SystemBarUtil;
import com.qwwuyu.lib.utils.ToastUtil;

public class MainActivity extends BaseActivity {
    private static int[] flag = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        TextView tv = findViewById(R.id.tv);
        tv.setText(CommUtil.getVersionName(mContext));
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
        ToastUtil.show("123");
    }

    public void onClick4(View v) {
        LogUtils.i(BuildConfig.TEST);
    }

    public void onClick5(View v) {
        int i = 2, num = 3, index = -1;
        if (flag[i] % num == ++index) {
            startActivity(new Intent(this, MvpActivity.class));
        } else if (flag[i] % num == ++index) {
            startActivity(new Intent(this, HttpActivity.class));
        }
        flag[i]++;
    }

    public void onClick6(View v) {
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