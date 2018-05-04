package com.qwwuyu.lib.clazz;

import android.view.animation.Interpolator;

public class CircleInterpolator implements Interpolator {
    private int frame = 0;
    private float last = 0;

    @Override
    public float getInterpolation(float input) {
        if (input == 0) return last = frame = 0;
        frame = frame + 1;
        float avg = input / frame;
        float avgAddFrame = 1f / (1f / avg + 1);
        float output = avgAddFrame * frame > 1 ? 1 : avgAddFrame * frame;
        output = output < last ? last : output;
        return last = output;
    }
}
