package com.qwwuyu.lib.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.qwwuyu.library.R;

import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * Created by qiwei on 2019/6/24 16:48.
 * Description 标题控件.
 */
public class TitleView extends ConstraintLayout {
    private TextView tvTitle;
    private ImageView ivBack;
    private View viewLine;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_title, this);
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        viewLine = findViewById(R.id.view_line);
        setBackgroundColor(0xffffffff);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(@StringRes int title) {
        tvTitle.setText(title);
    }

    public void setBackBtn() {
        ivBack.setOnClickListener(v -> {
            Context context = getContext();
            if (context instanceof Activity) ((Activity) context).finish();
        });
    }

    public void setBackBtn(Activity activity) {
        ivBack.setOnClickListener(v -> activity.finish());
    }

    public ImageView getBackImageView() {
        return ivBack;
    }

    public TextView getTitleTextView() {
        return tvTitle;
    }

    public void hintLine() {
        viewLine.setVisibility(GONE);
    }
}
