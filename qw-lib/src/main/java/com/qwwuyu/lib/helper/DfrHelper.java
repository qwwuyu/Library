package com.qwwuyu.lib.helper;

import com.qwwuyu.lib.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiwei on 2017/9/25
 */
public class DfrHelper {
    private static DfrHelper instance;
    private boolean enable = true;
    private Map<String, Long> map = new HashMap<>();

    private DfrHelper() {
    }

    public static DfrHelper getInstance() {
        if (instance == null) {
            synchronized (DfrHelper.class) {
                if (instance == null) instance = new DfrHelper();
            }
        }
        return instance;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void begin(String tag) {
        if (enable) {
            long nowTime = System.currentTimeMillis();
            map.put(tag, nowTime);
            LogUtils.log(LogUtils.I, true, tag, 4, "begin:" + nowTime);
        }
    }

    public void dfr(String tag) {
        dfr(tag, "");
    }

    public void dfr(String tag, Object append) {
        if (enable) {
            Long lastTime = map.get(tag);
            long nowTime = System.currentTimeMillis();
            map.put(tag, nowTime);
            if (lastTime != null) {
                LogUtils.log(LogUtils.I, true, tag + append, 4, "dfr:" + (nowTime - lastTime));
            } else {
                LogUtils.log(LogUtils.I, true, tag + append, 4, "no dfr:" + nowTime);
            }
        }
    }
}
