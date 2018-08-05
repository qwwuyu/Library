package com.qwwuyu.lib.recycler.adapter;

/**
 * Created by qiwei on 2018/8/3 20:34.
 * Description Adapter交换Item监听.
 */
public interface IMoveItem {
    /** 当交换Item */
    void moveItem(int fromPosition, int toPosition);
}
