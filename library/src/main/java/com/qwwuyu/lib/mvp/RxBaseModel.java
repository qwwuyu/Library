package com.qwwuyu.lib.mvp;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class RxBaseModel implements BaseModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RxBaseModel() {
    }

    public <T> void subscribe(final Observable<T> observable, final DisposableObserver<T> callback) {
        if (observable == null || callback == null) {
            return;
        }
        DisposableObserver[] ss = new DisposableObserver[1];
        DisposableObserver<T> subscribe = observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    DisposableObserver s = ss[0];
                    if (s != null) compositeDisposable.delete(s);
                })
                .subscribeWith(callback);
        ss[0] = subscribe;
        compositeDisposable.add(subscribe);
    }

    @Override
    public void destroy() {
        compositeDisposable.clear();
    }
}
