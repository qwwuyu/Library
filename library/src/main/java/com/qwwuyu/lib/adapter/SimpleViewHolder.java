package com.qwwuyu.lib.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 简单实现的ViewHolder.
 */
public class SimpleViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private Object object;

    public SimpleViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    /** 给TextView设置文本 */
    public <V extends TextView> V setText(int viewId, CharSequence text) {
        V view = getView(viewId);
        view.setText(text);
        return view;
    }

    /** 给TextView设置文本 */
    public <V extends TextView> V setText(int viewId, int resId) {
        return setText(viewId, context.getString(resId));
    }

    /** 给ImageView设置资源图片 */
    public <V extends ImageView> V setImageRes(int viewId, int resId) {
        V view = getView(viewId);
        view.setImageResource(resId);
        return view;
    }

    /** 给View设置点击事件 */
    public <V extends View> V setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        V view = getView(viewId);
        view.setOnClickListener(onClickListener);
        return view;
    }

    /** 给View设置隐藏显示 */
    public <V extends View> V setVisibility(int viewId, int visibility) {
        V view = getView(viewId);
        view.setVisibility(visibility);
        return view;
    }

    /** 给View设置背景资源 */
    public <V extends View> V setBackgroundResource(int viewId, int resId) {
        V view = getView(viewId);
        view.setBackgroundResource(resId);
        return view;
    }

    /** 给View设置激活 */
    public <V extends View> V setBackgroundResource(int viewId, boolean enabled) {
        V view = getView(viewId);
        view.setEnabled(enabled);
        return view;
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) itemView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            itemView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = itemView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (V) childView;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Context getContext() {
        return context;
    }
}
