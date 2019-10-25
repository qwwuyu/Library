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

    @Override
    public void destroy() {
        compositeDisposable.clear();
    }

    public <T> void subscribe(final Observable<T> observable, final DisposableObserver<T> callback) {
        if (observable == null || callback == null) {
            return;
        }
        compositeDisposable.add(toSubscribe(observable, callback));
    }

    /**
     * 设置订阅 和 所在的线程环境
     */
    public <T> DisposableObserver<T> toSubscribe(Observable<T> o, DisposableObserver<T> s) {
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(s);
    }
}
