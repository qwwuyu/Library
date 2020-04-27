package com.qwwuyu.example.database.rx2;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Zhang Tingkuo.
 * Date: 2017-04-28
 * Time: 14:28
 */
public class Rx2Dao<T, K> extends Rx2Base {

    private final AbstractDao<T, K> mDao;

    /**
     * Creates a new RxDao without a default scheduler.
     */
    public Rx2Dao(AbstractDao<T, K> dao) {
        this(dao, null);
    }

    /**
     * Creates a new RxDao with a default scheduler, which is used to configure returned observables with
     * {@link Observable#subscribeOn(Scheduler)}.
     */
    public Rx2Dao(AbstractDao<T, K> dao, Scheduler scheduler) {
        super(scheduler);
        mDao = dao;
    }

    /**
     * Rx version of {@link AbstractDao#loadAll()} returning an Observable.
     */
    public Observable<List<T>> loadAll() {
        return wrap(mDao::loadAll);
    }

    /**
     * Rx version of {@link AbstractDao#loadAll()} returning an Observable.
     */
    public Observable<T> load(final K key) {
        return wrap(() -> mDao.load(key));
    }

    /**
     * Rx version of {@link AbstractDao#refresh(Object)} returning an Observable.
     * Note that the Observable will emit the given entity back to its subscribers.
     */
    public Observable<T> refresh(final T entity) {
        return wrap(() -> {
            mDao.refresh(entity);
            return entity;
        });
    }

    /**
     * Rx version of {@link AbstractDao#insert(Object)} returning an Observable.
     * Note that the Observable will emit the given entity back to its subscribers.
     */
    public Observable<Long> insert(final T entity) {
        return wrap(() -> mDao.insert(entity));
    }

    /**
     * Rx version of {@link AbstractDao#insertInTx(Iterable)} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Iterable<T>> insertInTx(final Iterable<T> entities) {
        return wrap(() -> {
            mDao.insertInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#insertInTx(Object[])} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public final Observable<Object[]> insertInTx(final T... entities) {
        return wrap(() -> {
            mDao.insertInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#insertOrReplace(Object)} returning an Observable.
     * Note that the Observable will emit the given entity back to its subscribers.
     */
    public Observable<T> insertOrReplace(final T entity) {
        return wrap(() -> {
            mDao.insertOrReplace(entity);
            return entity;
        });
    }

    /**
     * Rx version of {@link AbstractDao#insertOrReplaceInTx(Iterable)} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Iterable<T>> insertOrReplaceInTx(final Iterable<T> entities) {
        return wrap(() -> {
            mDao.insertOrReplaceInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#insertOrReplaceInTx(Object[])} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Object[]> insertOrReplaceInTx(final T... entities) {
        return wrap(() -> {
            mDao.insertOrReplaceInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#save(Object)} returning an Observable.
     * Note that the Observable will emit the given entity back to its subscribers.
     */
    public Observable<T> save(final T entity) {
        return wrap(() -> {
            mDao.save(entity);
            return entity;
        });
    }

    /**
     * Rx version of {@link AbstractDao#saveInTx(Iterable)} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Iterable<T>> saveInTx(final Iterable<T> entities) {
        return wrap(() -> {
            mDao.saveInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#saveInTx(Object[])} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Object[]> saveInTx(final T... entities) {
        return wrap(() -> {
            mDao.saveInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#update(Object)} returning an Observable.
     * Note that the Observable will emit the given entity back to its subscribers.
     */
    public Observable<T> update(final T entity) {
        return wrap(() -> {
            mDao.update(entity);
            return entity;
        });
    }

    /**
     * Rx version of {@link AbstractDao#updateInTx(Iterable)} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Iterable<T>> updateInTx(final Iterable<T> entities) {
        return wrap(() -> {
            mDao.updateInTx(entities);
            return entities;
        });
    }

    /**
     * Rx version of {@link AbstractDao#updateInTx(Object[])} returning an Observable.
     * Note that the Observable will emit the given entities back to its subscribers.
     */
    public Observable<Object[]> updateInTx(final T... entities) {
        return wrap(() -> {
            mDao.updateInTx(entities);
            return entities;
        });
    }


    /**
     * Rx version of {@link AbstractDao#delete(Object)} returning an Observable.
     */
    public Observable<Boolean> delete(final T entity) {
        return wrap(() -> {
            mDao.delete(entity);
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#deleteByKey(Object)} returning an Observable.
     */
    public Observable<Boolean> deleteByKey(final K key) {
        return wrap(() -> {
            mDao.deleteByKey(key);
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#deleteAll()} returning an Observable.
     */
    public Observable<Boolean> deleteAll() {
        return wrap(() -> {
            mDao.deleteAll();
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#deleteInTx(Iterable)} returning an Observable.
     */
    public Observable<Boolean> deleteInTx(final Iterable<T> entities) {
        return wrap(() -> {
            mDao.deleteInTx(entities);
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#deleteInTx(Object[])} returning an Observable.
     */
    public final Observable<Boolean> deleteInTx(final T... entities) {
        return wrap(() -> {
            mDao.deleteInTx(entities);
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#deleteByKeyInTx(Iterable)} returning an Observable.
     */
    public Observable<Boolean> deleteByKeyInTx(final Iterable<K> keys) {
        return wrap(() -> {
            mDao.deleteByKeyInTx(keys);
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#deleteByKeyInTx(Object[])} returning an Observable.
     */
    public Observable<Boolean> deleteByKeyInTx(final K... keys) {
        return wrap(() -> {
            mDao.deleteByKeyInTx(keys);
            return true;
        });
    }

    /**
     * Rx version of {@link AbstractDao#count()} returning an Observable.
     */
    public Observable<Long> count() {
        return wrap(mDao::count);
    }

    /**
     * The plain DAO.
     */
    public AbstractDao<T, K> getDao() {
        return mDao;
    }
}
