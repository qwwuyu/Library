package com.qwwuyu.lib.utils.permit;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by qiwei on 2017/8/4
 */
public interface PermitCtrl {
    /**
     * 当需要请求权限前操作
     * @param showRationales 需要理由的权限
     * @param request        需要请求的权限,不为空
     * @return false 继续请求,true 拦截请求
     **/
    boolean beforeRequest(List<String> showRationales, @NonNull List<String> request, ProceedCallback proceedListener);

    /**
     * 权限全部通过
     */
    void onGranted();

    /**
     * 权限拒绝
     * @param granted       通过的权限
     * @param onlyDenied    禁止的权限
     * @param foreverDenied 不再询问禁止的权限
     */
    void onDenied(@NonNull List<String> granted, @NonNull List<String> onlyDenied, @NonNull List<String> foreverDenied);
}
