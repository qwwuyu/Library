package com.qwwuyu.lib.utils;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;

/**
 * Created by qiwei on 2018/5/4 14:51
 * Description .
 */
public class DrawableUtil {
    public static GradientDrawable getOvalDrawable(int fillColor, int width, @ColorInt int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setUseLevel(false);
        gd.setColor(fillColor);
        gd.setStroke(width, color);
        return gd;
    }
}
