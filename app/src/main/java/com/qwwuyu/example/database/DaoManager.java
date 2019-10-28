package com.qwwuyu.example.database;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库管理
 */
public class DaoManager {
    private static final String DB_NAME = "_db";
    private static DaoManager instance;

    private Context context;
    private Map<String, DaoSession> sessionMap = new HashMap<>();

    private DaoManager() {
    }

    public static DaoManager getInstance() {
        if (instance == null) {
            instance = new DaoManager();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public DaoSession getSession() {
        return getSession(null);
    }

    public DaoSession getSession(String _prefix) {
        String prefix = _prefix == null ? "public" : _prefix;
        DaoSession session = sessionMap.get(prefix);
        if (session == null) {
            synchronized (DaoManager.class) {
                session = sessionMap.get(prefix);
                if (session == null) {
                    UpgradeHelper devOpenHelper = new UpgradeHelper(context, prefix + DB_NAME, null);
                    DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
                    session = daoMaster.newSession();
                    sessionMap.put(prefix, session);
                }
            }
        }
        return session;
    }

    public void clearSession() {
        sessionMap.clear();
    }
}