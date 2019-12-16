package com.qwwuyu.lib.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.qwwuyu.library.R;

import java.lang.ref.WeakReference;


/**
 * 模式选择控件
 */
public class ModeView extends RelativeLayout implements View.OnClickListener {
    /** 行高 */
    private final int itemHeightDp = 27;
    /** 最大，最小，差值 */
    private int max, min, diff;
    /** 上次执行时间 */
    private long lastTime;
    /** 总共执行时间 */
    private final long time = 300;
    /** 是否打开状态 */
    private boolean isOpen = false;
    /** 执行执行完毕 */
    private boolean isOver = true;

    /** 当前模式 */
    private int[] modes;
    /** 控制界面文本 */
    private String[] modeTexts;

    private TextView[] tvs;
    private TableLayout table;
    private OnModeSelect onModeSelect;

    private final int MSG_CHANGE = 100;
    private Handler handler = new MyHandler(this);

    private void handleMessage(Message msg) {
        if (MSG_CHANGE == msg.what) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (!((params.height == max && isOpen) || (params.height == min && !isOpen))) {
                long nowTime = System.currentTimeMillis();
                long diffTime = nowTime - lastTime;
                lastTime = nowTime;
                int move = (int) (diffTime * diff / time);
                if (isOpen) {
                    params.height = params.height + move;
                    params.height = params.height > max ? max : params.height;
                } else {
                    params.height = params.height - move;
                    params.height = params.height < min ? min : params.height;
                }
                setLayoutParams(params);
                handler.sendEmptyMessage(MSG_CHANGE);
            } else {
                isOver = true;
            }
        }
    }

    public ModeView(Context context) {
        this(context, null);
    }

    public ModeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#80000000"));
        gd.setCornerRadius(dip2px(5));
        setBackgroundDrawable(gd);
        table = new TableLayout(context);
        addView(table, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE && tvs != null) {
            open(false);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeMessages(MSG_CHANGE);
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        if (isOpen && isOver) {
            int old = 0;
            int now = (Integer) v.getTag();
            int mode = modes[old];
            modes[old] = modes[now];
            modes[now] = mode;
            tvs[old].setText(modeTexts[modes[old]]);
            tvs[now].setText(modeTexts[modes[now]]);
            if (onModeSelect != null) onModeSelect.selectMode(modes[0]);
        }
        open(!isOpen);
    }

    public void setModes(int[] modes, String[] modeTexts) {
        if (tvs != null) return;
        this.modes = modes;
        this.modeTexts = modeTexts;
        tvs = new TextView[modes.length];
        Context context = getContext();
        min = dip2px(itemHeightDp);
        max = dip2px(itemHeightDp * modes.length);
        diff = max - min;
        for (int i = 0; i < tvs.length; i++) {
            TextView tv = new TextView(context);
            tvs[i] = tv;
            tv.setSingleLine();
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            tv.setTextSize(12);
            tv.setPadding(dip2px(12), 0, dip2px(10), 0);
            tv.setTextColor(0xffffffff);
            if (i == 0) {
                tv.setCompoundDrawablePadding(dip2px(2));
                Drawable nav_up = getResources().getDrawable(R.drawable.ic_del_press);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                tv.setCompoundDrawables(null, null, nav_up, null);
            }
            tv.setText(modeTexts[modes[i]]);
            tv.setTag(i);
            tv.setOnClickListener(this);
            TableRow row = new TableRow(context);
            row.addView(tv, new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, min));
            table.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void setOpen(boolean open) {
        isOpen = open;
        Drawable nav_up;
        if (isOpen) {
            nav_up = getResources().getDrawable(R.drawable.ic_del);
        } else {
            nav_up = getResources().getDrawable(R.drawable.ic_del_press);
        }
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tvs[0].setCompoundDrawables(null, null, nav_up, null);
    }

    public void setOnModeSelect(OnModeSelect onModeSelect) {
        this.onModeSelect = onModeSelect;
    }

    public boolean setDownEvent(MotionEvent event) {
        if ((!isOpen && isOver) || event.getAction() != MotionEvent.ACTION_DOWN || !isShown()) {
            return false;
        }
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
            return false;
        }
        open(false);
        return true;
    }

    private int dip2px(float dpValue) {
        return (int) (dpValue * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private void open(boolean isOpen) {
        isOver = false;
        lastTime = System.currentTimeMillis();
        setOpen(isOpen);
        handler.removeMessages(MSG_CHANGE);
        handler.sendEmptyMessage(MSG_CHANGE);
    }

    private static class MyHandler extends Handler {
        private WeakReference<ModeView> modeView;

        public MyHandler(ModeView modeView) {
            this.modeView = new WeakReference<>(modeView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ModeView cleanModeView = modeView.get();
            if (cleanModeView != null) {
                cleanModeView.handleMessage(msg);
            }
        }
    }

    /**
     * 模式选择回调
     */
    public interface OnModeSelect {
        void selectMode(int mode);
    }
}