package com.qwwuyu.lib.mvp;

import androidx.annotation.Nullable;

/**
 *
 */
public interface IStateLayout {
    /** 显示内容 */
    void showContentLayout();

    /** 显示加载条 */
    void showLoadingLayout(@Nullable CharSequence text);

    /** 显示空布局 */
    void showEmptyLayout(@Nullable CharSequence text);

    /** 显示出错布局 */
    void showErrorLayout(@Nullable CharSequence text);

    /** 显示网络问题布局 */
    void showNetworkLayout(@Nullable CharSequence text);
}
