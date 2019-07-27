package com.qwwuyu.lib.mvp;

/**
 * Created by qiwei on 2019/6/24 17:37.
 * Description .
 */
public interface IStateLayout {
    void showContentLayout();

    void showLoadingLayout();

    void showEmptyLayout();

    void showErrorLayout();

    void showNetworkLayout();
}
