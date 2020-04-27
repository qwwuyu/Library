package com.qwwuyu.example.database.rx2;

import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;

/**
 * Created by Zhang Tingkuo.
 * Date: 2017-04-28
 * Time: 14:20
 */
public class Rx2Query<T> extends Rx2Base {
    private final Query<T> mQuery;

    public Rx2Query(Query<T> query) {
        mQuery = query;
    }

    public Rx2Query(Query<T> query, Scheduler scheduler) {
        super(scheduler);
        mQuery = query;
    }

    /**
     * Rx version of {@link Query#list()} returning an Observable.
     */
    public Observable<List<T>> list() {
        return wrap(() -> mQuery.forCurrentThread().list());
    }

    /**
     * Rx version of {@link Query#unique()} returning an Observable.
     */
    public Observable<T> unique() {
        return wrap(() -> mQuery.forCurrentThread().unique());
    }

    /**
     * Emits the resulting entities one by one, producing them on the fly ("streaming" entities).
     * Unlike {@link #list()}, it does not wait for the query to gather all results. Thus, the first entities are
     * immediately available as soon the underlying database cursor has data. This approach may be more memory
     * efficient for large number of entities (or large entities) at the cost of additional overhead caused by a
     * per-entity delivery through Rx.
     */
    public Observable<T> oneByOne() {
        Observable<T> observable = Observable.create(emitter -> {
            try {
                try (LazyList<T> lazyList = mQuery.forCurrentThread().listLazyUncached()) {
                    for (T entity : lazyList) {
                        if (emitter.isDisposed()) {
                            break;
                        }
                        emitter.onNext(entity);
                    }
                }
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                emitter.onError(t);
            }
        });
        return wrap(observable);
    }
}
