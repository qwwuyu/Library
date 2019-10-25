package com.qwwuyu.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Description 不能左右滑动的ViewPager
 */
public class UnSlideViewpager extends ViewPager {
    private boolean unSlide = true;

    public UnSlideViewpager(Context context) {
        super(context);
    }

    public UnSlideViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUnSlide(boolean b) {
        this.unSlide = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !unSlide && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !unSlide && super.onInterceptHoverEvent(event);
    }
}