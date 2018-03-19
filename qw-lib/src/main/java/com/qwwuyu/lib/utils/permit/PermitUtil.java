package com.qwwuyu.lib.utils.permit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by qiwei on 2017/8/4
 */
public class PermitUtil {
    /**
     * @param activity    当前活动
     * @param permissions 请求的权限
     * @param ctrl        请求操作
     */
    public static void request(@NonNull final Activity activity, @NonNull final String[] permissions, @NonNull final PermitCtrl ctrl) {
        if (!isMarshmallow()) {
            ctrl.onGranted();
            return;
        }
        final ArrayList<String> request = new ArrayList<>();
        final ArrayList<String> showRationales = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                request.add(permission);
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    showRationales.add(permission);
                }
            }
        }
        if (request.isEmpty()) {
            ctrl.onGranted();
            return;
        }
        final PermitFragment fragment = PermitFragment.getPermissionsFragment(activity);
        final PermitFragment.OnCallback onCallback = new PermitFragment.OnCallback() {
            @Override
            public void onResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
                final ArrayList<String> granted = new ArrayList<>();
                final ArrayList<String> onlyDenied = new ArrayList<>();
                final ArrayList<String> foreverDenied = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                            onlyDenied.add(permissions[i]);
                        } else {
                            foreverDenied.add(permissions[i]);
                        }
                    } else {
                        granted.add(permissions[i]);
                    }
                }
                if (granted.size() == grantResults.length) {
                    ctrl.onGranted();
                } else {
                    ctrl.onDenied(granted, onlyDenied, foreverDenied);
                }
            }
        };
        final ProceedCallback proceedCallback = new ProceedCallback() {
            @Override
            public void proceed() {
                fragment.request(permissions, onCallback);
            }
        };
        if (!ctrl.beforeRequest(showRationales, request, proceedCallback)) {
            proceedCallback.proceed();
        }
    }

    private static boolean isRevoked(Context context, @NonNull String permName) {
        return isMarshmallow() && context.getPackageManager().isPermissionRevokedByPolicy(permName, context.getPackageName());
    }

    private static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /** 打开设置 */
    public static void openSetting(Context context, boolean newTask) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        if (newTask) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
