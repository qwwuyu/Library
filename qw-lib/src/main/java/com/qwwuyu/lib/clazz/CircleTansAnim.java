package com.qwwuyu.lib.clazz;

import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CircleTansAnim extends Animation {
    private float trans;

    public CircleTansAnim(float trans) {
        this.trans = trans;
    }

    @Override
    protected void applyTransformation(float time, Transformation t) {
        PointF p = time2Circle(time, trans);
        t.getMatrix().setTranslate(p.x, p.y - trans);
    }

    private PointF time2Circle(float interpolatedTime, float r) {
        PointF p = new PointF();
        float x = (float) (Math.sin(Math.PI * (1 - interpolatedTime) * 2) * r);
        float y = (float) (Math.cos(Math.PI * (1 - interpolatedTime) * 2) * r);
        p.set(x, y);
        return p;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        trans = width / 200f;
    }
}
