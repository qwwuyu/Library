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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * mvp Fragment基类view实现
 * ViewPager use {@link androidx.fragment.app.FragmentPagerAdapter#BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
 */
public abstract class LibMvpFragment<P extends BasePresenter> extends LibFragment implements BaseView {
    protected Context context;
    protected P presenter;
    protected boolean isLazyStart;

    private MvpConfig<P> mvpConfig;
    private TitleView titleView;
    private MultipleStateLayout stateLayout;
    private View rootView;
    private View contentView;
    private ProgressDialog loadingDialog;
    private int dialogCount = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (mvpConfig == null) {
            mvpConfig = new MvpConfig<>();
            initMvpConfig(mvpConfig);
            presenter = mvpConfig.presenter;
        }
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            initContent(context);
        } else if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mvpConfig.lazyCallBack != null) {
            isLazyStart = false;
            showLoadingLayout(null);
        }
        if (mvpConfig.titleCallBack != null) mvpConfig.titleCallBack.call(titleView);
        if (mvpConfig.stateCallBack != null) mvpConfig.stateCallBack.call(stateLayout);
        init(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mvpConfig.lazyCallBack != null && !isLazyStart) {
            isLazyStart = true;
            showContentLayout();
            mvpConfig.lazyCallBack.call();
        }
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) presenter.destroy();
        dialogCount = 0;
        super.onDestroyView();
    }

    /** MVP配置 */
    protected abstract void initMvpConfig(MvpConfig<P> mvpConfig);

    /** 初始化 */
    protected abstract void init(@Nullable Bundle savedInstanceState);

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
        if (mvpConfig.stateCallBack != null || mvpConfig.lazyCallBack != null) {
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
    public Context context() {
        return context;
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
}
