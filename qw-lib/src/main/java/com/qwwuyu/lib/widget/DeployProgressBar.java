package com.qwwuyu.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 配置界面进度条控件
 * Created by qiwei on 2017/8/24
 */
public class DeployProgressBar extends View {
    private final Paint bgPaint;
    private final Paint progressPaint;
    private int w, h;
    private RectF bgRect = new RectF();
    private RectF progressRect = new RectF();
    private int max = 100, progress = 0;

    public DeployProgressBar(Context context) {
        this(context, null);
    }

    public DeployProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeployProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(0xffcccccc);
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setProgress(int progress) {
        if (this.progress == progress) return;
        this.progress = progress;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    public void setProgress(int max, int progress) {
        this.max = max;
        this.progress = progress;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        bgRect.set(0, 0, w, h);
        progressPaint.setShader(new LinearGradient(0, 0, w, 0, new int[]{0xffF7BF67, 0xffF25A3F}, null, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(bgRect, bgRect.height() / 2, bgRect.height() / 2, bgPaint);
        progressRect.set(bgRect.left, bgRect.top, bgRect.right * progress / max, bgRect.bottom);
        canvas.drawRoundRect(progressRect, progressRect.height() / 2, progressRect.height() / 2, progressPaint);
    }
}
