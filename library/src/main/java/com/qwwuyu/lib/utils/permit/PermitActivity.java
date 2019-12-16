package com.qwwuyu.lib.utils.permit;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiwei on 2017/8/6
 * 仿造饿了么的首次请求权限
 */
public abstract class PermitActivity extends AppCompatActivity {
    private boolean isGranted = false;
    protected final String[] requests = requestPermissions();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermitUtil.request(this, requests, new PermitCtrl() {
            @Override
            public boolean beforeRequest(List<String> showRationales, @NonNull List<String> request, ProceedCallback proceedListener) {
                return firstBeforeRequest(showRationales, request, proceedListener);
            }

            @Override
            public void onGranted() {
                granted();
            }

            @Override
            public void onDenied(List<String> granted, List<String> onlyDenied, List<String> foreverDenied, List<String> denied) {
                singleRequest();
            }
        });
    }

    /** 继续单个请求权限 */
    protected void singleRequest() {
        final ArrayList<String> onlyDenied = new ArrayList<>();
        final ArrayList<String> foreverDenied = new ArrayList<>();
        for (String permission : requests) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    onlyDenied.add(permission);
                } else {
                    foreverDenied.add(permission);
                }
            }
        }
        if (!onlyDenied.isEmpty()) {
            PermitUtil.request(this, new String[]{onlyDenied.get(0)}, new PermitCtrl() {
                @Override
                public boolean beforeRequest(List<String> showRationales, @NonNull List<String> request, ProceedCallback proceedListener) {
                    return singleBeforeRequest(onlyDenied.get(0), proceedListener);
                }

                @Override
                public void onGranted() {
                    singleRequest();
                }

                @Override
                public void onDenied(List<String> granted, List<String> onlyDenied, List<String> foreverDenied, List<String> denied) {
                    singleRequest();
                }
            });
        } else if (!foreverDenied.isEmpty()) {
            onlyForeverDenied(foreverDenied, new ProceedCallback() {
                @Override
                public void proceed() {
                    PermitUtil.openSetting(PermitActivity.this, false);
                }
            });
        } else {
            granted();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (String permission : requests) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        granted();
    }

    private void granted() {
        if (!isGranted) {
            isGranted = true;
            onGranted();
        }
    }


    /**
     * @return 需要请求的权限
     */
    @NonNull
    protected abstract String[] requestPermissions();

    /**
     * 第一次请求权限
     * @param showRationales  需要提醒的权限
     * @param request         需求请求的权限
     * @param proceedListener 继续请求
     * @return false 继续请求,true 拦截请求
     */
    protected abstract boolean firstBeforeRequest(List<String> showRationales, List<String> request, ProceedCallback proceedListener);

    /**
     * @param request         需求请求的权限
     * @param proceedListener 继续请求
     * @return false 继续请求,true 拦截请求
     */
    protected abstract boolean singleBeforeRequest(String request, ProceedCallback proceedListener);

    /**
     * @param foreverDenied       永久拒绝的权限
     * @param openSettingListener 打开设置
     */
    protected abstract void onlyForeverDenied(List<String> foreverDenied, ProceedCallback openSettingListener);

    /**
     * 权限全部通过,仅回调一次
     */
    protected abstract void onGranted();
}