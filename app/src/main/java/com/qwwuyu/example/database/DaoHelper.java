package com.qwwuyu.example.database;


import com.qwwuyu.example.database.rx2.Rx2Dao;
import com.qwwuyu.example.database.rx2.Rx2Query;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;

import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by qiwei on 2019/8/19.
 */
public class DaoHelper {
    private static Scheduler dbScheduler;

    public static Scheduler getDbScheduler() {
        if (dbScheduler == null) {
            synchronized (DaoHelper.class) {
                if (dbScheduler == null) {
                    dbScheduler = Schedulers.from(Executors.newSingleThreadExecutor());
                }
            }
        }
        return dbScheduler;
    }

    public static <T> Rx2Query<T> query(Query<T> query) {
        return new Rx2Query<>(query, getDbScheduler());
    }

    public static <T, K> Rx2Dao<T, K> dao(AbstractDao<T, K> dao) {
        return new Rx2Dao<>(dao, getDbScheduler());
    }

    /** 清除内存缓存 */
    public static void clearDaoSession(DaoSession session) {
        getDbScheduler().scheduleDirect(session::clear);
    }
}
