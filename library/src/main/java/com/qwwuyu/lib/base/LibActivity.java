package com.qwwuyu.lib.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基类Activity
 */
public abstract class LibActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void finish() {
        IntentUtils.finish(this);
    }

    void finishSuper() {
        super.finish();
    }

    protected void addDisposable(@NonNull Disposable d) {
        compositeDisposable.add(d);
    }

    protected void clearDisposable() {
        compositeDisposable.clear();
    }
}
