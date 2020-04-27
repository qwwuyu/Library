package com.qwwuyu.lib.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.library.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * Created by qiwei on 2019/6/24 16:48.
 * Description 标题控件.
 */
public class TitleView extends ConstraintLayout {
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvCtrlRight;
    private ImageView ivCtrlRight;
    private ImageView ivCtrlRight2;
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
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvCtrlRight = findViewById(R.id.tv_ctrl_right);
        ivCtrlRight = findViewById(R.id.iv_ctrl_right);
        ivCtrlRight2 = findViewById(R.id.iv_ctrl_right2);
        viewLine = findViewById(R.id.view_line);

        setBackgroundColor(0xffffffff);
        ivBack.setOnClickListener(v -> {
            Activity activity = CommUtil.getActivity(getContext());
            if (activity != null) activity.finish();
        });
    }

    public ImageView getBackView() {
        return ivBack;
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    public TextView getRightTvView() {
        return tvCtrlRight;
    }

    public ImageView getRightIvView() {
        return ivCtrlRight;
    }

    public ImageView getRightIvView2() {
        return ivCtrlRight2;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(@StringRes int title) {
        tvTitle.setText(title);
    }

    public void setRightTxtClick(String txt, View.OnClickListener listener) {
        tvCtrlRight.setVisibility(VISIBLE);
        tvCtrlRight.setText(txt);
        tvCtrlRight.setOnClickListener(listener);
    }

    public void setRightIvClick(@DrawableRes int res, View.OnClickListener listener) {
        ivCtrlRight.setVisibility(VISIBLE);
        ivCtrlRight.setImageResource(res);
        ivCtrlRight.setOnClickListener(listener);
    }

    public void setRightIvClick2(@DrawableRes int res2, View.OnClickListener listener2) {
        ivCtrlRight2.setVisibility(VISIBLE);
        ivCtrlRight2.setImageResource(res2);
        ivCtrlRight2.setOnClickListener(listener2);
    }

    public void hintLine() {
        viewLine.setVisibility(GONE);
    }
}
