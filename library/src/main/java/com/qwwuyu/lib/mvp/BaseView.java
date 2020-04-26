package com.qwwuyu.lib.mvp;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 *
 */
public interface BaseView extends IStateLayout {
    /** 获取Context */
    Context getContext();

    /** 显示弹窗dialog */
    void showLoadingDialog(@Nullable CharSequence message);

    /** 隐藏弹窗dialog */
    void hideLoadingDialog();

    /** 显示信息提示 */
    void showMsg(int code, String msg);
}
