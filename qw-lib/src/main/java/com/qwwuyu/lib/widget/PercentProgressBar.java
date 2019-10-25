package com.qwwuyu.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 进度条控件
 * Created by qiwei on 2017/8/24
 */
public class PercentProgressBar extends View {
    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF bgRect = new RectF();
    private RectF progressRect = new RectF();
    private long max = 100, progress = 0;

    public PercentProgressBar(Context context) {
        this(context, null);
    }

    public PercentProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgPaint.setColor(0xffcccccc);
    }

    public long getProgress() {
        return progress;
    }

    public long getMax() {
        return max;
    }

    public void setProgress(long progress) {
        if (this.progress == progress) return;
        this.progress = Math.min(Math.max(0, progress), max);
        invalidate();
    }

    public void setProgress(long progress, long max) {
        this.max = Math.max(1, max);
        this.progress = Math.min(Math.max(0, progress), max);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bgRect.set(0, 0, w, h);
        progressPaint.setShader(new LinearGradient(0, 0, w, 0, new int[]{0xffDBAF59, 0xffff9977}, null, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(bgRect, bgRect.height() / 2, bgRect.height() / 2, bgPaint);
        progressRect.set(bgRect.left, bgRect.top, bgRect.right * progress / max, bgRect.bottom);
        canvas.drawRoundRect(progressRect, progressRect.height() / 2, progressRect.height() / 2, progressPaint);
    }
}
