package com.qwwuyu.example;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.qwwuyu.lib.utils.ToastUtil;
import com.qwwuyu.lib.utils.permit.PermitActivity;
import com.qwwuyu.lib.utils.permit.ProceedCallback;

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
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE};
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
                .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        proceedListener.proceed();
                    }
                }).create().show();
        return true;
    }

    @Override
    protected boolean singleBeforeRequest(String request, final ProceedCallback proceedListener) {
        new AlertDialog.Builder(ELMActivity.this)
                .setCancelable(false)
                .setTitle("请允许使用").setMessage(request)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        proceedListener.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
        return true;
    }

    @Override
    protected void onlyForeverDenied(List<String> foreverDenied, final ProceedCallback proceedListener) {
        StringBuilder message = new StringBuilder("为了您正常使用,需要以下权限");
        for (String permit : foreverDenied) {
            message.append("\n").append(permit);
        }
        new AlertDialog.Builder(ELMActivity.this)
                .setTitle("请在设置中打开").setMessage(message.toString())
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        proceedListener.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onGranted() {
        ToastUtil.show("onGranted");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
