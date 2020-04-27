package com.qwwuyu.lib.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 基类Activity
 */
public abstract class LibActivity extends AppCompatActivity {
    @Override
    public void finish() {
        IntentUtils.finish(this);
    }

    void finishSuper() {
        super.finish();
    }
}
