package com.qwwuyu.lib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Process;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * 通用的工具类
 */
public class CommUtil {
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isExist(CharSequence s) {
        return s != null && s.length() != 0;
    }

    /** 是否处于后台 */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        return !tasks.isEmpty() && !tasks.get(0).topActivity.getPackageName().equals(context.getPackageName());
    }

    /** 是否安装packageName的包 */
    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);//GET_UNINSTALLED_PACKAGES
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        } catch (Exception e) {//TransactionTooLargeException
            return true;
        }
    }

    /** 是否是主线程 */
    public static boolean isInMainProcess(Context context) {
        String processName = getProcessName(context, Process.myPid());
        String packageName = context.getPackageName();
        return processName == null || processName.length() == 0 || processName.equals(packageName);
    }

    /** 获取进程号对应的进程名 */
    private static String getProcessName(Context context, int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            return reader.readLine().trim();
        } catch (Throwable ignored) {
        } finally {
            try {
                reader.close();
            } catch (Exception ignored) {
            }
        }
        try {
            List<ActivityManager.RunningAppProcessInfo> runningApps = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            if (runningApps != null) {
                for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                    if (procInfo.pid == pid) return procInfo.processName;
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    /** 安装apk */
    public static void installApk(Context context, Uri apkPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(apkPath, "application/vnd.android.package-archive");
        context.startActivity(intent);
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
