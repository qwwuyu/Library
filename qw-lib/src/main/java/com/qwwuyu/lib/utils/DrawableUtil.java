package com.qwwuyu.lib.utils;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;

/**
 * Created by qiwei on 2018/5/4 14:51
 * Description .
 */
public class DrawableUtil {
    public static GradientDrawable getOval(int fillColor, int strokeWidth, @ColorInt int strokeColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(fillColor);
        if (strokeWidth != 0) gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    public static GradientDrawable getRectangleRadius(int fillColor, int radius) {
        return getRectangleRadius(fillColor, radius, 0, 0);
    }

    public static GradientDrawable getRectangleRadius(int fillColor, int radius,
                                                      int strokeWidth, @ColorInt int strokeColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(fillColor);
        gd.setCornerRadius(radius);
        if (strokeWidth != 0) gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }
}
