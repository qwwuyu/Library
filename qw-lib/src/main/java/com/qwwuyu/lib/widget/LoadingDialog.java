package com.qwwuyu.lib.widget;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qwwuyu.library.R;

/**
 * Created by qiwei on 2019/6/24 16:16.
 * Description 进度条.
 */
public class LoadingDialog extends AlertDialog {
    private boolean mCancelTouch;
    private boolean mCancelable;
    private View mView;
    private int mGravity;

    private TextView mMessageView;
    private CharSequence mMessage;

    private LoadingDialog(Builder builder) {
        this(builder, 0);
    }

    private LoadingDialog(Builder builder, int resStyle) {
        super(builder.mContext, resStyle);
        mCancelTouch = builder.mCancelTouch;
        mView = builder.mView;
        mGravity = builder.mGravity;
        mCancelable = builder.mCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mView == null) {
            mView = View.inflate(getContext(), R.layout.dialog_loading, null);
        }
        mMessageView = mView.findViewById(R.id.message);
        setMessage(mMessage);
        setContentView(mView);
        setCanceledOnTouchOutside(mCancelTouch);
        setCancelable(mCancelable);

        Window win = getWindow();
        if (win == null) {
            return;
        }
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = mGravity;
        win.setAttributes(lp);
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    public static final class Builder {
        private final Context mContext;
        private View mView;
        private int mResStyle;
        private int mGravity = Gravity.CENTER;
        private boolean mCancelTouch = false;
        private boolean mCancelable = true;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder view(int resId) {
            mView = LayoutInflater.from(mContext).inflate(resId, null);
            return this;
        }

        public Builder style(int resStyle) {
            this.mResStyle = resStyle;
            return this;
        }

        public Builder gravity(int gravity) {
            mGravity = gravity;
            return this;
        }

        public Builder cancelTouch(boolean cancelTouch) {
            mCancelTouch = cancelTouch;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public LoadingDialog build() {
            if (mResStyle != 0) {
                return new LoadingDialog(this, mResStyle);
            } else {
                return new LoadingDialog(this);
            }
        }
    }
}
