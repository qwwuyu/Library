package com.qwwuyu.lib.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by qiwei on 2019/8/5.
 * 基类Fragment
 */
public class LibFragment extends Fragment {
    protected Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
