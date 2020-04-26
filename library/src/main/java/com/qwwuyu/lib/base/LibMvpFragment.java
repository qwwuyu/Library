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
import com.qwwuyu.lib.utils.ToastUtil;
import com.qwwuyu.library.R;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * mvp Fragment基类view实现
 */
public abstract class LibMvpFragment<P extends BasePresenter> extends LibFragment implements BaseView {
    protected Context context;
    protected P presenter;
    protected boolean isLazyStart;
    private TitleView titleView;
    private View rootView;
    private View contentView;
    private MultipleStateLayout stateLayout;
    private ProgressDialog loadingDialog;
    private int dialogCount = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        if (!useLazy()) isLazyStart = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            initContent(getContext());
            if (useLazy() && !isLazyStart) {
                showLoadingLayout(null);
            }
        } else if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(contentView, titleView, stateLayout);
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) presenter.destroy();
        dialogCount = 0;
        super.onDestroyView();
    }

    /** 获取Presenter,最先执行,仅调用一次 */
    protected abstract P createPresenter();

    /** 获取布局resId */
    protected abstract int getContentLayout();

    /** 初始化 */
    protected abstract void init(View contentView, TitleView titleView, MultipleStateLayout stateLayout);

    /** TitleView */
    protected boolean useTitleView() {
        return false;
    }

    /** 使用多状态布局 */
    protected boolean useMultipleStateLayout() {
        return false;
    }

    /** 使用懒加载 */
    protected boolean useLazy() {
        return false;
    }

    /** 第一次加载 */
    protected void onLazy() {
    }

    @CallSuper
    @Override
    public void onVisibleChanged(boolean visible, boolean lifecycle, boolean isFirst) {
        if (useLazy() && visible && !isLazyStart) {
            isLazyStart = true;
            showContentLayout();
            onLazy();
        }
    }

    private void initContent(Context context) {
        final int contentId = getContentLayout();
        if (contentId == 0) {
            return;
        }
        ViewGroup rootView = null;
        if (useTitleView()) {
            titleView = new TitleView(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(titleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            rootView = linearLayout;
        }
        contentView = LayoutInflater.from(context).inflate(contentId, null, false);
        if (useMultipleStateLayout() || useLazy()) {
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
        this.rootView = rootView != null ? rootView : contentView;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void showLoadingDialog(@Nullable CharSequence message) {
        if (getActivity() == null || getActivity().isFinishing()) return;
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
        if (getActivity() == null || getActivity().isFinishing()) return;
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
        stateLayout.showContent(contentView);
    }

    @Override
    public void showLoadingLayout(@Nullable CharSequence text) {
        stateLayout.showLoading(contentView, text);
    }

    @Override
    public void showEmptyLayout(@Nullable CharSequence text) {
        stateLayout.showEmpty(contentView, text);
    }

    @Override
    public void showErrorLayout(@Nullable CharSequence text) {
        stateLayout.showError(contentView, text);
    }

    @Override
    public void showNetworkLayout(@Nullable CharSequence text) {
        stateLayout.showNetwork(contentView, text);
    }

    protected TitleView getTitleView() {
        return titleView;
    }

    protected MultipleStateLayout getStateLayout() {
        return stateLayout;
    }
}
