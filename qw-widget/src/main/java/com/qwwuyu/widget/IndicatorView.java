package com.qwwuyu.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by qiwei on 2017/8/26
 * 改于https://github.com/QMUI/QMUI_Android
 */
public class IndicatorView extends View {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mSize;
    private final RectF rectF = new RectF();

    private int mAnimateValue = 0;
    private ValueAnimator mAnimator;

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
        mPaint.setColor(0xfff3f3f3);
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
        if (mSize != w) {
            mSize = w;
            float part = mSize / 20, rectH = part * 5, rectW = rectH / 3;
            rectF.set(-rectW / 2, part - mSize / 2, rectW / 2, part + rectH - mSize / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mSize / 2, mSize / 2);
        canvas.rotate(mAnimateValue * 30);
        for (int i = 0; i < 12; i++) {
            mPaint.setAlpha(Math.min(255, 127 + 128 * i / 6));
            canvas.drawRoundRect(rectF, rectF.width() / 2, rectF.width() / 2, mPaint);
            canvas.rotate(30);
        }
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            if (value != mAnimateValue) {
                mAnimateValue = value;
                invalidate();
            }
        }
    };

    public void start() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, 12);
            mAnimator.addUpdateListener(mUpdateListener);
            mAnimator.setDuration(1000);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.start();
        } else if (!mAnimator.isStarted()) {
            mAnimator.start();
        }
    }

    public void stop() {
        if (mAnimator != null) {
            mAnimator.removeUpdateListener(mUpdateListener);
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }
}