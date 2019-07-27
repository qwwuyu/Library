package com.qwwuyu.lib.mvp;

/**
 * Created by qiwei on 2018/4/19 17:17
 * Description .
 */
public interface BaseView extends IStateLayout {
    void showLoadingDialog(CharSequence message);

    void hideLoadingDialog();

    void showError(String code, String msg);
}
