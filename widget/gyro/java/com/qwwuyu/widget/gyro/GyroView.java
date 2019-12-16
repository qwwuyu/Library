package com.qwwuyu.widget.gyro;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class GyroView extends PhotoView {
    private boolean firstRefresh = true;

    public GyroView(Context context) {
        super(context);
        initData();
    }

    public GyroView(Context context, AttributeSet attr) {
        super(context, attr);
        initData();
    }

    public GyroView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initData();
    }

    @TargetApi(21)
    public GyroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData();
    }

    private void initData() {
    }

    public void update(PhotoViewAttacher attacher) {
        if (firstRefresh) {
            firstRefresh = false;
            setImageBitmap(Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888));
            attacher.update();
        }
    }
}