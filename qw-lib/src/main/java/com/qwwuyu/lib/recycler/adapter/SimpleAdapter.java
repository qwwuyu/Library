package com.qwwuyu.lib.recycler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by qiwei on 2018/8/3 20:33.
 * Description 简单使用RecyclerView的通用Adapter.
 */
public abstract class SimpleAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> implements IMoveItem {
    /** 上下文 */
    protected final Context context;
    /** 数据 */
    protected final List<T> list;
    /** 布局填充器 */
    protected final LayoutInflater inflater;
    /** 布局id */
    protected final int itemLayoutId;
    /** Item监听 */
    protected final AdapterListener<T> adapterListener;

    public SimpleAdapter(@NonNull Context context, @NonNull List<T> list, int itemLayoutId) {
        this(context, list, itemLayoutId, null);
    }

    public SimpleAdapter(@NonNull Context context, @NonNull List<T> list, int itemLayoutId, AdapterListener<T> adapterListener) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        this.adapterListener = adapterListener;
        this.inflater = LayoutInflater.from(context);
        onCreate();
    }

    protected void onCreate() {
    }

    @Override
    public final int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public final SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimpleViewHolder(inflater.inflate(itemLayoutId, parent, false), context);
    }

    @Override
    public final void onBindViewHolder(@NonNull final SimpleViewHolder holder, int position) {
        final T t = list.get(position);
        onBind(position, holder, t);
        if (adapterListener != null) {
            holder.itemView.setOnClickListener(v -> adapterListener.onItemClick(position, v, t));
        }
    }

    public abstract void onBind(int position, SimpleViewHolder holder, T data);

    @Override
    public void moveItem(int formPosition, int toPosition) {
        int diff = formPosition < toPosition ? 1 : -1;
        for (int index = formPosition; index != toPosition; index += diff) {
            Collections.swap(list, index, index + diff);
        }
        notifyItemMoved(formPosition, toPosition);
    }

    public void addData(T t, int position) {
        list.add(position, t);
        notifyDataSetChanged();
    }

    public void addDataWithAnim(T t, int position) {
        list.add(position, t);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void removeDataWithAnim(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public int getColor(int color) {
        return context.getResources().getColor(color);
    }

    public float getDimension(int dimen) {
        return context.getResources().getDimension(dimen);
    }

    /** Item的点击监听 */
    public interface AdapterListener<T> {
        /** 当item某条目某控件被点击 */
        void onItemClick(int position, View v, T data);
    }
}
