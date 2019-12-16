package com.qwwuyu.lib.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.qwwuyu.library.R;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


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
        setEmptyListener(listener);
        setErrorListener(listener);
        setNetworkListener(listener);
    }

    public void setEmptyListener(OnClickListener emptyListener) {
        this.emptyListener = emptyListener;
        setRetryListener(emptyView, emptyListener);
    }

    public void setErrorListener(OnClickListener errorListener) {
        this.errorListener = errorListener;
        setRetryListener(errorView, errorListener);
    }

    public void setNetworkListener(OnClickListener networkListener) {
        this.networkListener = networkListener;
        setRetryListener(networkView, networkListener);
    }

    public final void showContent(View contentView) {
        showViewByStatus(null, contentView);
    }

    public final void showLoading(View contentView, CharSequence text) {
        if (loadingView == null) {
            loadingView = inflater.inflate(loadingViewResId, this, false);
        }
        setText(loadingView, text);
        showViewByStatus(loadingView, contentView);
    }

    public final void showEmpty(View contentView, CharSequence text) {
        if (null == emptyView) {
            emptyView = inflater.inflate(emptyViewResId, this, false);
            setRetryListener(emptyView, emptyListener);
        }
        setText(emptyView, text);
        showViewByStatus(emptyView, contentView);
    }

    public final void showError(View contentView, CharSequence text) {
        if (null == errorView) {
            errorView = inflater.inflate(errorViewResId, this, false);
            setRetryListener(errorView, errorListener);
        }
        setText(errorView, text);
        showViewByStatus(errorView, contentView);
    }

    public final void showNetwork(View contentView, CharSequence text) {
        if (null == networkView) {
            networkView = inflater.inflate(networkViewResId, this, false);
            setRetryListener(networkView, networkListener);
        }
        setText(networkView, text);
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
                lastVisibleView = loadingView = emptyView = errorView = networkView = null;
            }
        }
        if (visibleView != null) {
            if (visibleView.getParent() == null) addView(visibleView);
            visibleView.setVisibility(VISIBLE);
        }
        if (contentView != null) {
            contentView.setVisibility(visibleView == null ? VISIBLE : INVISIBLE);
        }
        lastVisibleView = visibleView;
    }

    private void setRetryListener(View layout, OnClickListener listener) {
        if (layout != null && listener != null) {
            View view = layout.findViewById(R.id.view_retry);
            (view != null ? view : layout).setOnClickListener(listener);
        }
    }

    private void setText(View layout, CharSequence text) {
        if (layout != null && text != null) {
            TextView tv = layout.findViewById(R.id.view_text);
            if (tv != null) tv.setText(text);
        }
    }
}
