package com.qwwuyu.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.qwwuyu.library.R;

/**
 * Created by qiwei on 2017/8/16
 */
public class GrdtView extends View {
    private final int TYPE_LT4RB = 0x00, TYPE_RT4LB = 0x01, TYPE_T4B = 0x02, TYPE_L4R = 0x03;
    private int type;
    private int w, h;
    private final int[] colors = new int[2];
    private final Paint paint = new Paint();
    private final int layerType = getLayerType();
    private Path grdtPath = new Path();

    public GrdtView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GrdtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GrdtView);
        type = a.getInteger(R.styleable.GrdtView_grl_type, TYPE_LT4RB);
        colors[0] = a.getColor(R.styleable.GrdtView_grl_color0, 0xffffffff);
        colors[1] = a.getColor(R.styleable.GrdtView_grl_color1, 0xff000000);
        a.recycle();
        paint.setAntiAlias(true);
        paint.setDither(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setGrdt(int type, int color0, int color1) {
        this.type = type;
        colors[0] = color0;
        colors[1] = color1;
        setShader();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        grdtPath.reset();
        grdtPath.addRect(new RectF(0, 0, w, h), Path.Direction.CW);
        setShader();
    }

    private void setShader() {
        if (type == TYPE_LT4RB) {
            paint.setShader(new LinearGradient(0, 0, w, h, colors, null, Shader.TileMode.CLAMP));
        } else if (type == TYPE_RT4LB) {
            paint.setShader(new LinearGradient(w, 0, 0, h, colors, null, Shader.TileMode.CLAMP));
        } else if (type == TYPE_T4B) {
            paint.setShader(new LinearGradient(0, 0, 0, h, colors, null, Shader.TileMode.CLAMP));
        } else if (type == TYPE_L4R) {
            paint.setShader(new LinearGradient(0, 0, w, 0, colors, null, Shader.TileMode.CLAMP));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(grdtPath, paint);
    }
}