package com.qwwuyu.lib.utils;

import com.qwwuyu.library.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiwei on 2017/9/25
 */
public class DfrUtil {
    private static DfrUtil dfrUtil;
    private boolean enable = true;
    private Map<String, Long> map = new HashMap<>();

    private DfrUtil() {
    }

    public static DfrUtil getInstance() {
        if (dfrUtil == null) {
            synchronized (DfrUtil.class) {
                if (dfrUtil == null) dfrUtil = new DfrUtil();
            }
        }
        return dfrUtil;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void begin(String tag) {
        if (BuildConfig.DEBUG && enable) {
            long nowTime = System.currentTimeMillis();
            map.put(tag, nowTime);
            LogUtils.log(LogUtils.I, tag, 4, "begin:" + nowTime);
        }
    }

    public void dfr(String tag) {
        dfr(tag, "");
    }

    public void dfr(String tag, Object append) {
        if (BuildConfig.DEBUG && enable) {
            Long lastTime = map.get(tag);
            long nowTime = System.currentTimeMillis();
            map.put(tag, nowTime);
            if (lastTime != null) {
                LogUtils.log(LogUtils.I, tag + append, 4, "dfr:" + (nowTime - lastTime));
            } else {
                LogUtils.log(LogUtils.I, tag + append, 4, "no dfr:" + nowTime);
            }
        }
    }
}
