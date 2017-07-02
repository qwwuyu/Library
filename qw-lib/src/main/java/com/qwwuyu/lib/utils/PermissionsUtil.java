package com.qwwuyu.lib.utils;

import android.support.v4.app.ActivityCompat;

/**
 * Created by qiwei on 2017/7/2
 */
public class PermissionsUtil {
    public static void test() {
        ActivityCompat.checkSelfPermission(null, null);
        ActivityCompat.requestPermissions(null, null, 0);
        ActivityCompat.shouldShowRequestPermissionRationale(null, null);
    }
}
