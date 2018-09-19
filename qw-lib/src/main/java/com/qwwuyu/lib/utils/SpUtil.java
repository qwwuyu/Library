package com.qwwuyu.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * sp数据操作工具类
 */
public class SpUtil {
    private static SpUtil spUtil;
    private static SharedPreferences sp;
    private static final String SP_NAME = "default";

    private SpUtil() {
        throw new UnsupportedOperationException("can't instantiate");
    }

    synchronized static void init(Context context, String name) {
        if (spUtil != null) return;
        spUtil = new SpUtil(context, name != null ? name : SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取一个操作sp数据的实例
     */
    public static SpUtil getSpUtil() {
        if (spUtil == null) {
            throw new UnsupportedOperationException("must init");
        }
        return spUtil;
    }

    private SpUtil(Context context, String name, int mode) {
        sp = context.getApplicationContext().getSharedPreferences(name, mode);
    }

    /**
     * sp保存一个int数据
     */
    public void putSPValue(String valueKey, int value) {
        sp.edit().putInt(valueKey, value).apply();
    }

    /**
     * sp保存一个float数据
     */
    public void putSPValue(String valueKey, float value) {
        sp.edit().putFloat(valueKey, value).apply();
    }

    /**
     * sp保存一个String数据
     */
    public void putSPValue(String valueKey, String value) {
        sp.edit().putString(valueKey, value).apply();
    }

    /**
     * sp保存一个boolean数据
     */
    public void putSPValue(String valueKey, boolean value) {
        sp.edit().putBoolean(valueKey, value).apply();
    }

    /**
     * sp保存一个long数据
     */
    public void putSPValue(String valueKey, long value) {
        sp.edit().putLong(valueKey, value).apply();
    }

    /**
     * sp获取一个int数据
     */
    public int getSPValue(String valueKey, int value) {
        return sp.getInt(valueKey, value);
    }

    /**
     * sp获取一个float数据
     */
    public float getSPValue(String valueKey, float value) {
        return sp.getFloat(valueKey, value);
    }

    /**
     * sp获取一个String数据
     */
    public String getSPValue(String valueKey, String value) {
        return sp.getString(valueKey, value);
    }

    /**
     * sp获取一个boolean数据
     */
    public boolean getSPValue(String valueKey, boolean value) {
        return sp.getBoolean(valueKey, value);
    }

    /**
     * sp获取一个long数据
     */
    public long getSPValue(String valueKey, long value) {
        return sp.getLong(valueKey, value);
    }

    /**
     * 清理sp数据
     */
    public void clear() {
        sp.edit().clear().apply();
    }
}