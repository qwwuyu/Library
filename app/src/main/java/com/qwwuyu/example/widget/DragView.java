package com.qwwuyu.example.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.qwwuyu.lib.utils.LogUtils;

import androidx.annotation.Nullable;

public class DragView extends View implements View.OnTouchListener {
    private static final float FLOAT_NAN = Float.MIN_VALUE;
    private final int minPadding;
    private ViewGroup viewGroup;
    private ViewGroup.MarginLayoutParams lp;
    private int parentHeight, parentWidth;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        minPadding = dp2px(30);
        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (viewGroup == null) {
            viewGroup = (ViewGroup) getParent();
            lp = (ViewGroup.MarginLayoutParams) viewGroup.getLayoutParams();
            View parent = (View) viewGroup.getParent();
            parentHeight = parent.getHeight();
            parentWidth = parent.getWidth();
        }
    }

    private float rawX, rawY, leftMargin, topMargin;
    private float x0, y0, x1, y1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setTranslate(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN | 0x0100:
                x0 = event.getX(0);
                y0 = event.getY(0);
                x1 = event.getX(1);
                y1 = event.getY(1);
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    if (Float.MIN_VALUE == rawX) {
                        setTranslate(event.getRawX(), event.getRawY());
                    }
                    setMargin((int) (leftMargin + event.getRawX() - rawX), (int) (topMargin + event.getRawY() - rawY));
                } else if (event.getPointerCount() == 2) {

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_UP | 0x0100:
                rawX = Float.MIN_VALUE;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                LogUtils.i("MotionEvent:" + event.getAction());
                break;
        }
        return true;
    }

    private void setTranslate(float rawX, float rawY) {
        this.rawX = rawX;
        this.rawY = rawY;
        leftMargin = lp.leftMargin;
        topMargin = lp.topMargin;
    }

    private void setMargin(int leftMargin, int topMargin) {
        if (lp.leftMargin != leftMargin || lp.topMargin != topMargin) {
            lp.leftMargin = leftMargin;
            lp.topMargin = topMargin;
            limit();
        }
    }

    private void limit() {
        lp.leftMargin = Math.max(lp.leftMargin, minPadding - lp.width);
        lp.leftMargin = Math.min(lp.leftMargin, parentWidth - minPadding);
        lp.topMargin = Math.max(lp.topMargin, minPadding + -lp.height);
        lp.topMargin = Math.min(lp.topMargin, parentHeight - minPadding);
        viewGroup.setLayoutParams(lp);
    }

    private int dp2px(float dpValue) {
        return (int) (dpValue * getResources().getDisplayMetrics().density + 0.5f);
    }
}
