package com.qwwuyu.example.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;


import com.qwwuyu.example.R;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * 传递context默认弹dialog,根据cancelRequest取消弹窗并取消请求.
 */
public abstract class OnBaseSub extends DisposableObserver<ResponseBody> implements DialogInterface.OnDismissListener {
    /** 取消弹窗的时候同时取消请求 */
    private final boolean cancelRequest;
    private boolean encrypt = false;
    private Context context;
    private ProgressDialog progressDialog;

    public OnBaseSub() {
        this(null);
    }

    /**
     * @param context 不为空时默认弹窗
     */
    public OnBaseSub(Context context) {
        this(context, false);
    }

    /**
     * @param context       上下文
     * @param cancelRequest 是否取消请求后续操作
     */
    public OnBaseSub(Context context, boolean cancelRequest) {
        this.context = context;
        this.cancelRequest = cancelRequest;
        if (context != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.dialog_hint_default));
            if (cancelRequest) {
                progressDialog.setOnDismissListener(this);
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.setOnDismissListener(null);
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public final void onStart() {
        showProgressDialog();
        onStart1();
    }

    @Override
    public final void onError(Throwable e) {
        dismissProgressDialog();
        onError1(e);
    }

    @Override
    public final void onComplete() {
        dismissProgressDialog();
        onComplete1();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    protected void onStart1() {
    }

    abstract void onError1(@NonNull Throwable e);

    protected void onComplete1() {
    }
}
