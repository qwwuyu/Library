package com.qwwuyu.lib.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebView;

public class CookieUtils {
    private static boolean init = false;

    public static void init(Context context) {
        if (!init) {
            init = true;
            //在4.4以下版本使用CookieManager.getInstance().getCookie() 需要先创建WebView
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                new WebView(context);
            }
            CookieManager.setAcceptFileSchemeCookies(true);
        }
    }

    public static void syncCookie(Context context, String[] cookies, String... urls) {
        init(context);
        CookieManager cookieManager = CookieManager.getInstance();
        for (String cookie : cookies) {
            if (!TextUtils.isEmpty(cookie)) {
                for (String url : urls) {
                    cookieManager.setCookie(url, cookie + ";");
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        }
    }

    public static void removeAllCookie(Context context) {
        init(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }
}
