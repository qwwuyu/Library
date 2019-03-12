package com.github.chrisbanes.photoview;

import android.view.MotionEvent;

public class OnDoubleTapRevertListener implements OnDoubleTapListener {
    private PhotoViewAttacher attacher;
    private boolean isOnlyRevert = false;

    public OnDoubleTapRevertListener(PhotoViewAttacher attacher) {
        this.attacher = attacher;
    }

    public OnDoubleTapRevertListener(PhotoViewAttacher attacher, boolean isOnlyRevert) {
        this.attacher = attacher;
        this.isOnlyRevert = isOnlyRevert;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        try {
            float scale = attacher.getScale();
            float x = ev.getX();
            float y = ev.getY();
            if (scale <= attacher.getMediumScale() * 1.02f && scale >= attacher.getMediumScale() * 0.98f && !isOnlyRevert) {
                attacher.setScale(attacher.getMaximumScale(), x, y, true);
            } else {
                attacher.setScale(attacher.getMediumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }
        return true;
    }
}