package com.qwwuyu.lib.helper;

import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * 从网上整理的一套解决fragment嵌套显示bug的方案
 * Fragment的mUserVisibleHint属性控制器，用于准确的监听Fragment是否对用户可见
 *
 * mUserVisibleHint属性有什么用？
 * 使用ViewPager时我们可以通过Fragment的getUserVisibleHint()&&isResume()方法来判断用户是否能够看见某个Fragment
 * 利用这个特性我们可以更精确的统计页面的显示事件和标准化页面初始化流程（真正对用户可见的时候才去请求数据）
 *
 * 解决BUG
 * FragmentUserVisibleController还专门解决了在Fragment或ViewPager嵌套ViewPager时子Fragment的mUserVisibleHint属性与父Fragment的mUserVisibleHint属性不同步的问题
 * 例如外面的Fragment的mUserVisibleHint属性变化时，其包含的ViewPager中的Fragment的mUserVisibleHint属性并不会随着改变，这是ViewPager的BUG
 *
 * 使用方式（假设你的基类Fragment是MyFragment）：
 * 1. 在你的MyFragment的构造函数中New一个FragmentUserVisibleController（一定要在构造函数中new）
 * 2. 重写Fragment的onActivityCreated()、onResume()、onPause()、setUserVisibleHint(boolean)、onHiddenChanged(boolean)方法，
 * 调用FragmentUserVisibleController的activityCreated()、isResume()、pause()、setUserVisibleHint(boolean)、onHiddenChanged(boolean)方法
 * 3. 实现FragmentUserVisibleController.UserVisibleCallback接口并实现以下方法
 * void waitingShowToUser(Boolean)：直接调用FragmentUserVisibleController#waitingShowToUser(Boolean)即可
 * void onVisibleToUserChanged(boolean, boolean)：当Fragment对用户可见或不可见的就会回调此方法，你可以在这个方法里记录页面显示日志或初始化页面
 */
public class FragmentVisibleHelper {
    private boolean mWaitingShowToUser;
    private Fragment mFragment;
    private UserVisibleCallback mUserVisibleCallback;
    private boolean firstVisible = true;

    public FragmentVisibleHelper(Fragment fragment, UserVisibleCallback userVisibleCallback) {
        this.mFragment = fragment;
        this.mUserVisibleCallback = userVisibleCallback;
    }

    private boolean checkFirstVisible() {
        if (firstVisible) {
            firstVisible = false;
            return true;
        } else {
            return false;
        }
    }

    public void activityCreated() {
        // 如果自己是显示状态，但父Fragment却是隐藏状态，就把自己也改为隐藏状态，并且设置一个等待显示的标记
        Fragment parentFragment = mFragment.getParentFragment();
        if (mFragment.getUserVisibleHint() && parentFragment != null && !parentFragment.getUserVisibleHint()) {
            mWaitingShowToUser = true;
            parentFragment.setUserVisibleHint(false);
        }
    }

    public void resume() {
        if (mFragment.getUserVisibleHint()) {
            mUserVisibleCallback.onVisibleChanged(mFragment.isVisible(), true, checkFirstVisible());
        }
    }

    public void pause() {
        if (mFragment.getUserVisibleHint()) {
            mUserVisibleCallback.onVisibleChanged(false, true, checkFirstVisible());
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (mFragment.isResumed()) {
            mUserVisibleCallback.onVisibleChanged(isVisibleToUser, false, checkFirstVisible());
        }

        if (mFragment.getActivity() != null) {
            List<Fragment> childFragmentList = mFragment.getChildFragmentManager().getFragments();
            for (Fragment childFragment : childFragmentList) {
                if (childFragment instanceof UserVisibleCallback) {
                    UserVisibleCallback userVisibleCallback = (UserVisibleCallback) childFragment;
                    if (isVisibleToUser && userVisibleCallback.waitingShowToUser(null)) {
                        // 将所有正等待显示的子Fragment设置为显示状态，并取消等待显示标记
                        userVisibleCallback.waitingShowToUser(false);
                        childFragment.setUserVisibleHint(true);
                    } else if (!isVisibleToUser && childFragment.getUserVisibleHint()) {
                        // 将所有正在显示的子Fragment设置为隐藏状态，并设置一个等待显示标记
                        userVisibleCallback.waitingShowToUser(true);
                        childFragment.setUserVisibleHint(false);
                    }
                }
            }
        }
    }

    public void onHiddenChanged(boolean hidden) {
        mUserVisibleCallback.onVisibleChanged(!hidden && mFragment.isResumed(), false, checkFirstVisible());
    }

    public boolean waitingShowToUser(Boolean waitingShowToUser) {
        if (waitingShowToUser != null) {
            mWaitingShowToUser = waitingShowToUser;
        }
        return mWaitingShowToUser;
    }

    public interface UserVisibleCallback {
        boolean waitingShowToUser(Boolean waitingShowToUse);

        void onVisibleChanged(boolean visible, boolean lifecycle, boolean isFirst);
    }
}