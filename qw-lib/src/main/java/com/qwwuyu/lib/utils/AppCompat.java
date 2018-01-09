package com.qwwuyu.lib.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by qiwei on 2017/6/30
 */
@SuppressWarnings("deprecation")
public class AppCompat {
    public static Spanned fromHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}