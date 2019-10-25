package com.qwwuyu.lib.stadyTest;

import androidx.fragment.app.Fragment;

import com.qwwuyu.lib.utils.permit.PermitUtil;
import com.qwwuyu.lib.utils.permit.ProceedCallback;
import com.qwwuyu.lib.utils.permit.SPermitCtrl;

import org.junit.Test;

import java.util.List;

public class PermitTest {
    @Test
    public void useAppContext() throws Exception {
        //简单的请求,只处理成功结果
        PermitUtil.request((Fragment) null, null, new SPermitCtrl() {
            @Override
            public void onGranted() {
                super.onGranted();
            }
        });
        //拦截请求弹窗提示
        PermitUtil.request((Fragment) null, null, new SPermitCtrl() {
            @Override
            public boolean beforeRequest(List<String> showRationales, List<String> request, ProceedCallback proceedListener) {
                //弹窗回调proceedListener
                proceedListener.proceed();
                return true;
            }
        });
        //处理请求失败
        PermitUtil.request((Fragment) null, new String[]{}, new SPermitCtrl() {
            @Override
            public void onDenied(List<String> granted, List<String> onlyDenied, List<String> foreverDenied, List<String> denied) {
                //处理请求失败
            }
        });
    }
}
