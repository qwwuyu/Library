package com.qwwuyu.lib.utils.permit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.SparseArray;

/**
 * Created by qiwei on 2017/8/4
 */
public class PermitFragment extends Fragment {
    private static final String TAG = "Permissions";
    private static int requestCode = 10000;
    private SparseArray<OnCallback> callbacks = new SparseArray<>();

    private synchronized static int getRequestCode() {
        return requestCode++;
    }

    /**
     * @return 获取权限工具PermitFragment
     */
    static PermitFragment getPermissionsFragment(Activity activity) {
        PermitFragment permitFragment = (PermitFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        if (permitFragment == null) {
            permitFragment = new PermitFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().add(permitFragment, TAG).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return permitFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(Build.VERSION_CODES.M)
    void request(@NonNull String[] permissions, OnCallback callback) {
        int requestCode = getRequestCode();
        callbacks.put(requestCode, callback);
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnCallback onCallback = callbacks.get(requestCode);
        if (onCallback != null) {
            callbacks.delete(requestCode);
            onCallback.onResult(permissions, grantResults);
        }
    }

    public interface OnCallback {
        /**
         * 请求权限结果
         */
        void onResult(@NonNull String[] permissions, @NonNull int[] grantResults);
    }
}