package com.qwwuyu.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.qwwuyu.library.R;

/**
 * Created by qiwei on 2019/6/24 11:49.
 * Description 多状态布局.
 */
public class MultipleStateLayout extends FrameLayout {
    private int emptyViewResId;
    private int errorViewResId;
    private int loadingViewResId;
    private int networkViewResId;
    private boolean removeState;

    private View loadingView;
    private View emptyView;
    private View errorView;
    private View networkView;
    private View lastVisibleView;

    private LayoutInflater inflater;
    private OnClickListener emptyListener;
    private OnClickListener errorListener;
    private OnClickListener networkListener;

    public MultipleStateLayout(@NonNull Context context) {
        this(context, null);
    }

    public MultipleStateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStateLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStateLayout);
        emptyViewResId = a.getResourceId(R.styleable.MultipleStateLayout_emptyView, R.layout.view_empty);
        errorViewResId = a.getResourceId(R.styleable.MultipleStateLayout_errorView, R.layout.view_error);
        loadingViewResId = a.getResourceId(R.styleable.MultipleStateLayout_loadingView, R.layout.view_loading);
        networkViewResId = a.getResourceId(R.styleable.MultipleStateLayout_networkView, R.layout.view_network);
        removeState = a.getBoolean(R.styleable.MultipleStateLayout_removeState, false);
        a.recycle();
        inflater = LayoutInflater.from(getContext());
    }

    public void setEmptyViewResId(int emptyViewResId) {
        this.emptyViewResId = emptyViewResId;
    }

    public void setErrorViewResId(int errorViewResId) {
        this.errorViewResId = errorViewResId;
    }

    public void setLoadingViewResId(int loadingViewResId) {
        this.loadingViewResId = loadingViewResId;
    }

    public void setNetworkViewResId(int networkViewResId) {
        this.networkViewResId = networkViewResId;
    }

    public void setRemoveState(boolean removeState) {
        this.removeState = removeState;
    }

    public void setAllListener(OnClickListener listener) {
        this.emptyListener = listener;
        this.errorListener = listener;
        this.networkListener = listener;
    }

    public void setEmptyListener(OnClickListener emptyListener) {
        this.emptyListener = emptyListener;
    }

    public void setErrorListener(OnClickListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setNetworkListener(OnClickListener networkListener) {
        this.networkListener = networkListener;
    }

    public final void showContent(View contentView) {
        showViewByStatus(null, contentView);
    }

    public final void showLoading(View contentView) {
        if (loadingView == null) {
            loadingView = inflater.inflate(loadingViewResId, this);
        }
        showViewByStatus(loadingView, contentView);
    }

    public final void showEmpty(View contentView) {
        if (null == emptyView) {
            emptyView = inflater.inflate(emptyViewResId, this);
            setRetryListener(emptyView, R.id.retry_empty, emptyListener);
        }
        showViewByStatus(emptyView, contentView);
    }

    public final void showError(View contentView) {
        if (null == errorView) {
            errorView = inflater.inflate(errorViewResId, this);
            setRetryListener(errorView, R.id.retry_error, errorListener);
        }
        showViewByStatus(errorView, contentView);
    }

    public final void showNetwork(View contentView) {
        if (null == networkView) {
            networkView = inflater.inflate(networkViewResId, this);
            setRetryListener(networkView, R.id.retry_network, networkListener);
        }
        showViewByStatus(networkView, contentView);
    }

    private void showViewByStatus(View visibleView, View contentView) {
        if (lastVisibleView == visibleView) {
            return;
        }
        if (lastVisibleView != null) {
            lastVisibleView.setVisibility(GONE);
            if (removeState) {
                removeView(lastVisibleView);
                lastVisibleView = null;
            }
        }
        if (visibleView != null) {
            visibleView.setVisibility(VISIBLE);
        }
        if (contentView != null) {
            contentView.setVisibility(visibleView == null ? VISIBLE : INVISIBLE);
        }
        lastVisibleView = visibleView;
    }

    private void setRetryListener(View layout, int retry, OnClickListener listener) {
        if (listener != null) {
            View view = layout.findViewById(retry);
            (view != null ? view : layout).setOnClickListener(listener);
        }
    }
}
