package com.qwwuyu.example.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qiwei on 2019/6/28.
 * 工作台item控件
 */
public class WorkItemView extends View {
    /** 矩形路径 */
    private Path rectPath = new Path();
    /** 三角形路径 */
    private Path trianglePath = new Path();
    /** 背景画笔 */
    private Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /** 文本画笔 */
    private Paint txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /** 文本边缘 */
    private Rect txtBounds = new Rect();
    /** 数据传递 */
    private RectF rectF = new RectF();
    /** 矩形颜色 */
    private int rectColor = 0;
    /** 三角颜色 */
    private int triangleColor = 0;
    /** 文本 */
    private int txt = 0;
    /** 常量距离 */
    private final float radius;
    private final float txtLeft;
    private final float txtTop;

    public WorkItemView(Context context) {
        this(context, null);
    }

    public WorkItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public WorkItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radius = dp2px(5);
        txtLeft = dp2px(10);
        txtTop = dp2px(8);
        colorPaint.setStyle(Paint.Style.FILL);

        trianglePath.lineTo(dp2px(36), 0);
        trianglePath.lineTo(0, dp2px(39));
        trianglePath.close();

        txtPaint.setTextSize(15 * getResources().getDisplayMetrics().density);
        txtPaint.setFakeBoldText(true);
        txtPaint.setColor(0xffffffff);
        txtPaint.getTextBounds("1", 0, 1, txtBounds);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rectPath.reset();
        rectF.set(0, 0, getWidth(), getHeight());
        rectPath.addRoundRect(rectF, radius, radius, Path.Direction.CCW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(rectPath, Region.Op.INTERSECT);
        colorPaint.setColor(rectColor);
        canvas.drawPath(rectPath, colorPaint);
        colorPaint.setColor(triangleColor);
        canvas.drawPath(trianglePath, colorPaint);
        canvas.restore();
        canvas.drawText(String.valueOf(txt), txtLeft - txtBounds.left, txtTop - txtBounds.top, txtPaint);
    }

    private float dp2px(float dpValue) {
        return dpValue * getResources().getDisplayMetrics().density;
    }

    public void setData(int rectColor, int triangleColor, int txt) {
        this.rectColor = rectColor;
        this.triangleColor = triangleColor;
        this.txt = txt;
        invalidate();
    }
}
