package com.qwwuyu.lib.widget.banner;

import android.content.Context;
import android.view.View;

/**
 * Created by qiwei on 2019/8/12.
 */
public abstract class BannerAdapter {
    public abstract int getCount();

    public abstract Integer getType(int index);

    public abstract View createView(Context context, int index);

    public abstract void initView(View view, int index);

    public abstract void onClick(int index);
}
