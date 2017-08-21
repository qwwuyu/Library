package com.qwwuyu.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * Created by qiwei on 2017/8/21
 */
public class ColorPickerView extends View {
    private Bitmap bitmap;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bitmap = makeBitmap(optimalBitmapSize());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    private int optimalBitmapSize() {
        final int scale = 2;
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        return Math.min(dm.widthPixels, dm.heightPixels) / scale;
    }

    private static Bitmap makeBitmap(int radiusPx) {
        int[] colors = new int[radiusPx * radiusPx];
        float[] hsv = new float[]{0f, 0f, 1f};
        for (int y = 0; y < radiusPx; ++y) {
            for (int x = 0; x < radiusPx; ++x) {
                int i = x + y * radiusPx;
                float sat = satForPos(x, y, radiusPx);
                int alpha = (int) (Math.max(0, Math.min(1, (1 - sat) * radiusPx)) * 255); // antialias edge
                if (alpha > 0) {
                    hsv[0] = hueForPos(x, y, radiusPx);
                    hsv[1] = sat;
                    colors[i] = Color.HSVToColor(alpha, hsv);
                }
            }
        }
        return Bitmap.createBitmap(colors, radiusPx, radiusPx, Bitmap.Config.ARGB_8888);
    }


    private static float hueForPos(float x, float y, float radiusPx) {
        final double r = radiusPx / 2;
        double hue = Math.toDegrees((Math.atan2(y - r, x - r)));
        if (hue < 0) hue += 360;
        return (float) hue;
    }

    private static float satForPos(float x, float y, float radiusPx) {
        final double r = radiusPx / 2; // gives values 0...1 inclusive
        final double dx = (x - r) / r;
        final double dy = (y - r) / r;
        final double sat = dx * dx + dy * dy; // leave it squared -- exaggerates pale colors
        return (float) sat;
    }
}