package com.qwwuyu.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qiwei on 2017/8/26
 */
public class IndicatorView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rectF = new RectF();
    private int startValue = 0xfff3f3f3, endValue = 0x80f3f3f3;
    private float size = 0;
    private Bitmap bitmap;
    private long beginTime = 0;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Constrain to square
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        final int h = MeasureSpec.getSize(heightMeasureSpec);
        final int modeW = MeasureSpec.getMode(widthMeasureSpec);
        final int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int size;
        if (modeW == MeasureSpec.UNSPECIFIED) {
            size = h;
        } else if (modeH == MeasureSpec.UNSPECIFIED) {
            size = w;
        } else {
            size = Math.min(w, h);
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (size != w) {
            size = w;
            float part = size / 20, rectH = part * 5, rectW = rectH / 3;
            rectF.set(-rectW / 2, part - size / 2, rectW / 2, part + rectH - size / 2);
            bitmap = Bitmap.createBitmap((int) size, (int) size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.translate(size / 2, size / 2);
            for (int i = 0; i < 12; i++) {
                float fraction = (float) i / 6 > 1 ? 1 : (float) i / 6;
                paint.setColor(evaluate(fraction, startValue, endValue));
                canvas.drawRoundRect(rectF, rectW / 2, rectW / 2, paint);
                canvas.rotate(-30);
            }
            canvas.save();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(size / 2, size / 2);
        canvas.rotate((int) (rate(beginTime, 1000) * 12) * 30);
        canvas.drawBitmap(bitmap, -size / 2, -size / 2, null);
        invalidate();
    }

    private float rate(long beginTime, long diff) {
        return (float) ((System.currentTimeMillis() - beginTime) % diff) / diff;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        beginTime = System.currentTimeMillis();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        beginTime = System.currentTimeMillis();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private int evaluate(float fraction, int startValue, int endValue) {
        int startA = startValue >>> 24;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;
        int endA = endValue >>> 24;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) | ((startB + (int) (fraction * (endB - startB))));
    }
}