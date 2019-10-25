package com.qwwuyu.lib.widget.banner;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qiwei on 2019/8/12.
 */
public class ClickListener implements View.OnTouchListener {
    private final View view;
    private final float diff;
    private boolean isMove = false;
    private boolean isTouch = false;
    private float beginX;
    private float beginY;
    private OnClickListener listener;
    private long lastClickTime;

    public ClickListener(View view, OnClickListener listener) {
        this.view = view;
        this.listener = listener;
        diff = view.getResources().getDisplayMetrics().density * 10;
    }

    @Override
    public boolean onTouch(View _view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (listener.onTouch()) {
                    isTouch = true;
                    isMove = true;
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    beginX = x;
                    beginY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove && (Math.abs(x - beginX) > diff || Math.abs(y - beginY) > diff)) {
                    isMove = false;
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMove && (Math.abs(x - beginX) <= diff && Math.abs(y - beginY) <= diff)) {
                    long time = System.currentTimeMillis();
                    if (time - lastClickTime > 1000L) {
                        lastClickTime = time;
                        listener.onClick();
                    }
                }//注意此处无break
            case MotionEvent.ACTION_CANCEL:
                if (isMove) {
                    isMove = false;
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (isTouch) {
                    isTouch = false;
                    listener.onFinish();
                }
                break;
        }
        return false;
    }

    public interface OnClickListener {
        void onClick();

        boolean onTouch();

        void onFinish();
    }
}
