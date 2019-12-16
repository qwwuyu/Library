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
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements BaseView {
    protected Context context = BaseMvpActivity.this;
    protected P presenter;
    private TitleView titleView;
    private View contentView;
    private View stateContent;
    private MultipleStateLayout stateLayout;
    private ProgressDialog loadingDialog;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
        initContent(this);
        SystemBarUtil.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));
        SystemBarUtil.setStatusBarDarkMode(this, true);
        init(savedInstanceState, titleView, stateLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.destroy();
    }

    /** 获取Presenter,最先执行,仅调用一次 */
    protected abstract P createPresenter();

    /** 获取布局resId */
    protected abstract int getContentLayout();

    /** 初始化 */
    protected abstract void init(Bundle bundle, TitleView titleView, MultipleStateLayout stateLayout);

    /** TitleView */
    protected boolean useTitleView() {
        return false;
    }

    /** 使用多状态布局 */
    protected boolean useMultipleStateLayout() {
        return false;
    }

    private void initContent(Context context) {
        final int contentId = getContentLayout();
        final boolean useTitle = useTitleView();
        final boolean useState = useMultipleStateLayout();
        if (contentId == 0) {
            return;
        }
        ViewGroup rootView = null;
        if (useTitle) {
            titleView = new TitleView(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(titleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            rootView = linearLayout;
        }
        contentView = LayoutInflater.from(context).inflate(contentId, null, false);
        if (useState) {
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
        stateContent = contentView;
        setContentView(rootView != null ? rootView : contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void showLoadingDialog(@Nullable CharSequence message) {
        if (!isFinishing() && loadingDialog == null && context != null) {
            loadingDialog = new ProgressDialog(context);
            loadingDialog.setMessage(message == null ? getString(R.string.dialog_hint_default) : message);
            loadingDialog.show();
        } else if (!isFinishing() && loadingDialog != null) {
            loadingDialog.setMessage(message == null ? getString(R.string.dialog_hint_default) : message);
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (!isFinishing() && loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showError(int code, String msg) {
        ToastUtil.show(msg);
    }

    @Override
    public void showContentLayout() {
        if (stateLayout != null) stateLayout.showContent(stateContent);
    }

    @Override
    public void showLoadingLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showLoading(stateContent, text);
    }

    @Override
    public void showEmptyLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showEmpty(stateContent, text);
    }

    @Override
    public void showErrorLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showError(stateContent, text);
    }

    @Override
    public void showNetworkLayout(@Nullable CharSequence text) {
        if (stateLayout != null) stateLayout.showNetwork(stateContent, text);
    }

    protected void setStateView(MultipleStateLayout stateLayout, View stateContent) {
        this.stateLayout = stateLayout;
        this.stateContent = stateContent;
    }

    protected TitleView getTitleView() {
        return titleView;
    }

    protected MultipleStateLayout getStateLayout() {
        return stateLayout;
    }
}
