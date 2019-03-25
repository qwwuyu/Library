package com.qwwuyu.example.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.qwwuyu.example.MainActivity;
import com.qwwuyu.example.R;
import com.qwwuyu.lib.utils.ToastUtil;
import com.qwwuyu.lib.utils.permit.PermitActivity;
import com.qwwuyu.lib.utils.permit.ProceedCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiwei on 2017/8/6
 */
public class ELMActivity extends PermitActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_elm);
    }

    @NonNull
    @Override
    protected String[] requestPermissions() {
        List<String> requests = new ArrayList<>();
        requests.add(Manifest.permission.CAMERA);
        requests.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            requests.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return requests.toArray(new String[0]);
    }

    @Override
    protected boolean firstBeforeRequest(List<String> showRationales, List<String> request, final ProceedCallback proceedListener) {
        StringBuilder message = new StringBuilder("为了您正常使用,需要以下权限");
        for (String permit : request) {
            message.append("\n").append(permit);
        }
        new AlertDialog.Builder(ELMActivity.this)
                .setCancelable(false)
                .setTitle("开启").setMessage(message.toString())
                .setPositiveButton("下一步", (dialog, which) -> proceedListener.proceed()).create().show();
        return true;
    }

    @Override
    protected boolean singleBeforeRequest(String request, final ProceedCallback proceedListener) {
        new AlertDialog.Builder(ELMActivity.this)
                .setCancelable(false)
                .setTitle("请允许使用").setMessage(request)
                .setPositiveButton("确定", (dialog, which) -> proceedListener.proceed())
                .setNegativeButton("取消", (dialog, which) -> finish()).create().show();
        return true;
    }

    @Override
    protected void onlyForeverDenied(List<String> foreverDenied, final ProceedCallback proceedListener) {
        StringBuilder message = new StringBuilder("为了您正常使用,需要以下权限");
        for (String permit : foreverDenied) {
            message.append("\n").append(permit);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(ELMActivity.this)
                .setCancelable(false)
                .setTitle("请在设置中打开").setMessage(message.toString())
                .setPositiveButton("去设置", null).setNegativeButton("拒绝", null)
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> proceedListener.proceed());
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> finish());
    }

    @Override
    protected void onGranted() {
        ToastUtil.show("onGranted");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
