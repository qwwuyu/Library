package com.qwwuyu.lib.clazz;

import android.view.View;

public interface VisibleView {
    default boolean isVisible() {
        return this instanceof View && ((View) this).getVisibility() == View.VISIBLE;
    }
}
