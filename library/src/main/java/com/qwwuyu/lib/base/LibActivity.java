package com.qwwuyu.lib.base;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 基类Activity
 */
public abstract class LibActivity extends AppCompatActivity {
    protected Context context = LibActivity.this;

    @Override
    public void finish() {
        IntentUtils.finish(this);
    }

    void finishSuper() {
        super.finish();
    }
}
