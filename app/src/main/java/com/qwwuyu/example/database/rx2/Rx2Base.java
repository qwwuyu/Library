package com.qwwuyu.example.database.rx2;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Zhang Tingkuo.
 * Date: 2017-04-28
 * Time: 14:14
 */
class Rx2Base {

    protected final Scheduler mScheduler;

    /**
     * No default scheduler.
     */
    public Rx2Base() {
        mScheduler = null;
    }

    /**
     * Sets the default scheduler, which is used to configure returned observables with
     * {@link Observable#subscribeOn(Scheduler)}.
     */
    Rx2Base(Scheduler scheduler) {
        mScheduler = scheduler;
    }

    /**
     * The default scheduler (or null) used for wrapping.
     */
    public Scheduler getScheduler() {
        return mScheduler;
    }

    protected <R> Observable<R> wrap(Callable<R> callable) {
        return wrap(fromCallable(callable));
    }

    protected <R> Observable<R> wrap(Observable<R> observable) {
        if (mScheduler != null) {
            return observable.subscribeOn(mScheduler).observeOn(AndroidSchedulers.mainThread());
        } else {
            return observable;
        }
    }

    private static <T> Observable<T> fromCallable(final Callable<T> callable) {
        return Observable.defer(new Callable<ObservableSource<T>>() {
            @Override
            public ObservableSource<T> call() {
                T result;
                try {
                    result = callable.call();
                } catch (Exception e) {
                    return Observable.error(e);
                }
                return Observable.just(result);
            }
        });
    }
}
