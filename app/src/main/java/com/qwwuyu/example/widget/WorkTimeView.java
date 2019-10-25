package com.qwwuyu.example.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by qiwei on 2019/6/28.
 * 工作台时间控件
 */
public class WorkTimeView extends View {
    /** 画笔 */
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /** 周画笔 */
    private Paint weekPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /** 数据传递 */
    private RectF rectF = new RectF();
    /** 文字界限 */
    private Rect dayBounds = new Rect();
    private Rect slashBounds = new Rect();
    private Rect monthBounds = new Rect();
    private Rect nowBounds = new Rect();
    private Rect weekBounds = new Rect();
    /** 常量距离 */
    private final float paddingTop;
    private final float paddingRight;
    private final String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private int w;
    private int h;
    private String day;
    private String slash;
    private String month;
    private String week;
    private String now;

    public WorkTimeView(Context context) {
        this(context, null);
    }

    public WorkTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public WorkTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paddingTop = dp2px(11);
        paddingRight = dp2px(12);
        reset();
    }

    @SuppressLint("DefaultLocale")
    public void reset() {
        Calendar calendar = Calendar.getInstance();
        day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        slash = "/";
        month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        week = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        now = "今天";
        paint.setColor(0xff000000);
        paint.setFakeBoldText(true);
        paint.setTextSize(40 * getResources().getDisplayMetrics().density);
        paint.getTextBounds(day, 0, day.length(), dayBounds);
        paint.setTextSize(23 * getResources().getDisplayMetrics().density);
        paint.getTextBounds(slash, 0, slash.length(), slashBounds);
        paint.setTextSize(25 * getResources().getDisplayMetrics().density);
        paint.getTextBounds(month, 0, month.length(), monthBounds);

        paint.setFakeBoldText(false);
        paint.setTextSize(16 * getResources().getDisplayMetrics().density);
        paint.getTextBounds(now, 0, now.length(), nowBounds);
        paint.getTextBounds(week, 0, week.length(), weekBounds);

        float w1 = dayBounds.width() + slashBounds.width() + monthBounds.width();
        float w2 = nowBounds.width() + paddingRight + weekBounds.width();
        w = (int) (Math.max(w1, w2) + dp2px(2));
        h = (int) (dayBounds.height() + paddingTop + nowBounds.height() + dp2px(2));
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(dp2px(1), dp2px(1));
        paint.setFakeBoldText(true);
        paint.setTextSize(40 * getResources().getDisplayMetrics().density);
        canvas.drawText(day, -dayBounds.left, -dayBounds.top, paint);
        paint.setTextSize(23 * getResources().getDisplayMetrics().density);
        canvas.drawText(slash, dayBounds.width() - slashBounds.left,
                dayBounds.height() - slashBounds.height() - slashBounds.top, paint);
        paint.setTextSize(25 * getResources().getDisplayMetrics().density);
        canvas.drawText(month, dayBounds.width() + slashBounds.width() - monthBounds.left,
                dayBounds.height() - monthBounds.height() - monthBounds.top, paint);
        paint.setFakeBoldText(false);
        paint.setTextSize(16 * getResources().getDisplayMetrics().density);
        canvas.drawText(now, nowBounds.left, dayBounds.height() + paddingTop - nowBounds.top, paint);
        canvas.drawText(week, nowBounds.width() + paddingRight + weekBounds.left,
                dayBounds.height() + paddingTop - weekBounds.top, paint);
    }

    private float dp2px(float dpValue) {
        return dpValue * getResources().getDisplayMetrics().density;
    }
}
