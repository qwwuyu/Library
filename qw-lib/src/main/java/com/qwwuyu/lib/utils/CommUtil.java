package com.qwwuyu.lib.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPInputStream;

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

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception ignored) {
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception ignored) {
        }
        return 0;
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
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                for (ActivityManager.RunningAppProcessInfo procInfo : activityManager.getRunningAppProcesses()) {
                    if (procInfo.pid == pid) return procInfo.processName;
                }
            }
        } catch (Throwable ignored) {
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            return reader.readLine().trim();
        } catch (Throwable ignored) {
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /** 安装apk */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /** Gzip解压 */
    public static byte[] gzipUnCompress(byte[] bt) {
        ByteArrayOutputStream byteAos = null;
        ByteArrayInputStream byteArrayIn = null;
        GZIPInputStream gzipIn = null;
        try {
            byteArrayIn = new ByteArrayInputStream(bt);
            gzipIn = new GZIPInputStream(byteArrayIn);
            byteAos = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            int temp;
            while ((temp = gzipIn.read(b)) > 0) {
                byteAos.write(b, 0, temp);
            }
        } catch (Exception e) {
            return null;
        } finally {
            closeStream(byteAos, gzipIn, byteArrayIn);
        }
        return byteAos.toByteArray();
    }

    /** close */
    public static void closeStream(Object... streams) {
        for (Object stream : streams) {
            try {
                if (stream instanceof Closeable) ((Closeable) stream).close();
            } catch (IOException ignored) {
            }
        }
    }

    /** 是否处于后台 */
    @Deprecated
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ComponentName topComponentName = getTopComponentName(context);
        return topComponentName != null && !topComponentName.getPackageName().equals(context.getPackageName());
    }

    public static String getTopActivity(Context context) {
        ComponentName topComponentName = getTopComponentName(context);
        if (topComponentName != null) {
            return topComponentName.toString();
        }
        return "";
    }

    private static ComponentName getTopComponentName(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return null;
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.get(0).topActivity;
        }
        return null;
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
