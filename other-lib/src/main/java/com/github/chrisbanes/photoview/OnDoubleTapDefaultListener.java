package com.github.chrisbanes.photoview;

import android.view.MotionEvent;

public class OnDoubleTapDefaultListener implements OnDoubleTapListener {
    private PhotoViewAttacher attacher;

    public OnDoubleTapDefaultListener(PhotoViewAttacher attacher) {
        this.attacher = attacher;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        try {
            float scale = attacher.getScale();
            float x = ev.getX();
            float y = ev.getY();
            if (attacher.isEdgeMax()) {
                attacher.setScale(attacher.getMinimumScale(), x, y, true);
            } else if (scale <= attacher.getMediumScale() * 0.99f) {
                attacher.setScale(attacher.getMediumScale(), x, y, true);
            } else if (scale >= attacher.getMediumScale() * 0.99f && scale <= attacher.getMaximumScale() * 0.99f) {
                attacher.setScale(attacher.getMaximumScale(), x, y, true);
            } else {
                attacher.setScale(attacher.getMinimumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }
        return true;
    }
}