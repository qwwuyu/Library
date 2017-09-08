package com.qwwuyu.lib.stadyTest;

import android.support.annotation.NonNull;

import com.qwwuyu.lib.utils.permit.PermitUtil;
import com.qwwuyu.lib.utils.permit.ProceedCallback;
import com.qwwuyu.lib.utils.permit.SimplePermitCtrl;

import org.junit.Test;

import java.util.List;

public class PermitTest {
    @Test
    public void useAppContext() throws Exception {
        //简单的请求,只处理成功结果
        PermitUtil.request(null, null, new SimplePermitCtrl() {
            @Override
            public void onGranted() {
            }
        });
        //拦截请求弹窗提示
        PermitUtil.request(null, null, new SimplePermitCtrl() {
            @Override
            public boolean beforeRequest(List<String> showRationales, @NonNull List<String> request, ProceedCallback proceedListener) {
                //弹窗回调proceedListener
                proceedListener.proceed();
                return true;
            }
        });
        //处理请求失败
        PermitUtil.request(null, new String[]{}, new SimplePermitCtrl() {
            @Override
            public void onDenied(@NonNull List<String> granted, @NonNull List<String> onlyDenied, @NonNull List<String> foreverDenied) {
                //处理请求失败
            }
        });
    }
}
