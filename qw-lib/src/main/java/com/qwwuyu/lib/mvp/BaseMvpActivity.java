package com.qwwuyu.lib.mvp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.qwwuyu.lib.widget.LoadingDialog;
import com.qwwuyu.lib.widget.MultipleStateLayout;
import com.qwwuyu.lib.widget.TitleView;


/**
 * Created by qiwei on 2018/4/19 17:48
 * Description .
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected Context context = BaseMvpActivity.this;
    protected P presenter;
    private TitleView titleView;
    private View contentView;
    private MultipleStateLayout stateLayout;
    private LoadingDialog loadingDialog;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
        initContent();
        init(savedInstanceState, titleView, stateLayout);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) presenter.destroy();
        super.onDestroy();
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

    private void initContent() {
        final int contentId = getContentLayout();
        final boolean useTitle = useTitleView();
        final boolean useState = useMultipleStateLayout();
        if (contentId == 0) {
            return;
        }
        ViewGroup rootView = null;
        if (useTitle) {
            titleView = new TitleView(this);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(titleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            rootView = linearLayout;
        }
        contentView = LayoutInflater.from(this).inflate(contentId, null, false);
        if (useState) {
            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            stateLayout = new MultipleStateLayout(this);
            frameLayout.addView(stateLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
    public void showLoadingDialog(CharSequence message) {
        if (!isFinishing() && loadingDialog == null && context != null) {
            loadingDialog = new LoadingDialog.Builder(context).build();
            loadingDialog.setMessage(message);
            loadingDialog.show();
        } else if (!isFinishing() && loadingDialog != null) {
            loadingDialog.setMessage(message);
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
    public void showError(String code, String msg) {

    }

    @Override
    public void showContentLayout() {
        stateLayout.showContent(contentView);
    }

    @Override
    public void showLoadingLayout() {
        stateLayout.showLoading(contentView);
    }

    @Override
    public void showEmptyLayout() {
        stateLayout.showEmpty(contentView);
    }

    @Override
    public void showErrorLayout() {
        stateLayout.showError(contentView);
    }

    @Override
    public void showNetworkLayout() {
        stateLayout.showNetwork(contentView);
    }

    public TitleView getTitleView() {
        return titleView;
    }

    public MultipleStateLayout getStateLayout() {
        return stateLayout;
    }
}
