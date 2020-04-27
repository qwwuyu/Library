package com.qwwuyu.lib.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qwwuyu.lib.mvp.BasePresenter;
import com.qwwuyu.lib.mvp.BaseView;
import com.qwwuyu.lib.utils.SystemBarUtil;
import com.qwwuyu.lib.utils.ToastUtil;
import com.qwwuyu.library.R;

import androidx.annotation.Nullable;

/**
 * mvp Activity基类view实现
 */
public abstract class LibMvpActivity<P extends BasePresenter> extends LibActivity implements BaseView {
    protected Context context = LibMvpActivity.this;
    protected P presenter;

    private MvpConfig<P> mvpConfig = new MvpConfig<>();
    private TitleView titleView;
    private MultipleStateLayout stateLayout;
    private View contentView;
    private ProgressDialog loadingDialog;
    private int dialogCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMvpConfig(mvpConfig);
        presenter = mvpConfig.presenter;
        initContent(context);
        SystemBarUtil.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));
        SystemBarUtil.setStatusBarDarkMode(this, true);
        if (mvpConfig.titleCallBack != null) mvpConfig.titleCallBack.call(titleView);
        if (mvpConfig.stateCallBack != null) mvpConfig.stateCallBack.call(stateLayout);
        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.destroy();
    }

    /** MVP配置 */
    protected abstract void initMvpConfig(MvpConfig<P> mvpConfig);

    /** 初始化 */
    protected abstract void init(Bundle savedInstanceState);

    private void initContent(Context context) {
        final int contentId = mvpConfig.layoutResID;
        if (contentId == 0) {
            return;
        }
        ViewGroup rootView = null;
        if (mvpConfig.titleCallBack != null) {
            titleView = new TitleView(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(titleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            rootView = linearLayout;
        }
        contentView = LayoutInflater.from(context).inflate(contentId, null, false);
        if (mvpConfig.stateCallBack != null) {
            FrameLayout frameLayout = new FrameLayout(context);
            stateLayout = new MultipleStateLayout(context);
            frameLayout.addView(stateLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            frameLayout.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if (rootView != null) {
                rootView.addView(frameLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            } else {
                rootView = frameLayout;
            }
        } else if (rootView != null) {
            rootView.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        setContentView(rootView != null ? rootView : contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public Context context() {
        return context;
    }

    @Override
    public void showLoadingDialog(@Nullable CharSequence message) {
        if (isFinishing()) return;
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(context);
        }
        if (dialogCount++ == 0) {
            loadingDialog.setMessage(message == null ? getString(R.string.dialog_hint_default) : message);
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (isFinishing()) return;
        if (--dialogCount == 0) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMsg(int code, String msg) {
        ToastUtil.show(msg);
    }

    @Override
    public void showContentLayout() {
        if (stateLayout != null) stateLayout.showContent(contentView);
    }

    @Override
    public void showLoadingLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showLoading(contentView, text);
    }

    @Override
    public void showEmptyLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showEmpty(contentView, text);
    }

    @Override
    public void showErrorLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showError(contentView, text);
    }

    @Override
    public void showNetworkLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showNetwork(contentView, text);
    }
}
