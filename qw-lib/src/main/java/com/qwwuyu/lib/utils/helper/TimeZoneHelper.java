package com.qwwuyu.lib.utils.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * 时区转换工具
 */
public class TimeZoneHelper {
    private static String utcTxt = "UTC";
    private static TimeZone utc = TimeZone.getTimeZone(utcTxt);
//    private static TimeZone local = TimeZone.getDefault();

    public static String getUtcTxt() {
        return utcTxt;
    }

    public static TimeZone getLocal() {
        return TimeZone.getDefault();
    }

    public static TimeZone getUTC() {
        return utc;
    }

    public static int[] localToUTC(int hour, int minute) {
        return transferTime(hour, minute, TimeZone.getDefault(), utc);
    }

    public static TimeZone s2tz(String timeZoneId) {
        return TimeZone.getTimeZone(timeZoneId);
    }

    /**
     * @param hour       小时
     * @param minute     分钟
     * @param timeZone   时间时区
     * @param targetZone 转换目标时区
     * @return 转换后时间
     **/
    public static int[] transferTime(int hour, int minute, TimeZone timeZone, TimeZone targetZone) {
        long time = System.currentTimeMillis();
        int targetTime = hour * 60 + minute - timeZone.getOffset(time) / 60000 + targetZone.getOffset(time) / 60000;
        int diff = 0;
        if (targetTime < 0) {
            targetTime = targetTime + 1440;
            diff = -1;
        } else if (targetTime >= 1440) {
            targetTime = targetTime - 1440;
            diff = 1;
        }
        return new int[]{targetTime / 60, targetTime % 60, diff};
    }

    /** 不同地区时间 星期也不一样 */
    public static List<Integer> transferWeek(List<Integer> list, int diff) {
        List<Integer> show = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Integer d = list.get(i) + diff;
            if (d <= 0) show.add(d + 7);
            else if (d >= 8) show.add(d - 7);
            else show.add(d);
        }
        return show;
    }
}
