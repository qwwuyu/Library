package com.qwwuyu.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.qwwuyu.lib.utils.LogUtils;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库更新管理
 */
public class UpgradeHelper extends DaoMaster.OpenHelper {
    public UpgradeHelper(Context context, String name) {
        super(context, name);
    }

    public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * init: {@link KeyValue}
     */
    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
        LogUtils.i("onCreate");
    }

    /**
     * update1~2:
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtils.i("onUpgrade:oldVersion=" + oldVersion + " newVersion:" + newVersion);
        switch (oldVersion) {
            case 1://v1->v2
                //no break
            case 2:
        }
    }
}
