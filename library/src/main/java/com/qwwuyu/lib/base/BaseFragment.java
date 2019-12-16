package com.qwwuyu.lib.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by qiwei on 2019/8/5.
 * 基类Fragment
 */
public class BaseFragment extends Fragment implements FragmentVisibleHelper.UserVisibleCallback {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FragmentVisibleHelper controller = new FragmentVisibleHelper(this, this);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controller.activityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        controller.pause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        controller.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean waitingShowToUser(Boolean waitingShowToUser) {
        return controller.waitingShowToUser(waitingShowToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        controller.onHiddenChanged(hidden);
    }

    @Override
    public void onVisibleChanged(boolean visible, boolean lifecycle, boolean isFirst) {

    }

    @Override
    public void onDestroyView() {
        clearDisposable();
        super.onDestroyView();
    }

    protected void addDisposable(@NonNull Disposable d) {
        compositeDisposable.add(d);
    }

    protected void clearDisposable() {
        compositeDisposable.clear();
    }
}
