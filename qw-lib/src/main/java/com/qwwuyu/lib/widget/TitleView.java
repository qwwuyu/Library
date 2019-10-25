package com.qwwuyu.lib.widget;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;

import com.qwwuyu.library.R;

/**
 * Created by qiwei on 2019/6/24 16:48.
 * Description 标题控件.
 */
public class TitleView extends ConstraintLayout {
    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_title, this);
    }
}
