package com.qwwuyu.lib.stadyTest;

import org.junit.Test;

/**
 * Created by qiwei on 2018/4/24 18:08
 * Description 工具测试.
 */
public class UnitTest {
    @Test
    public void test() throws Exception {
        System.out.println(getAngle(0, 0, 1, 1));
        System.out.println(getAngle(0, 0, -1, 1));
        System.out.println(getAngle(0, 0, 1, -1));
        System.out.println(getAngle(0, 0, -1, -1));
    }

    @Test
    public void str2ascii() throws Exception {
        String s = "测试";
        for (int i = 0; i < s.length(); i++) {
            System.out.print("\\u" + Integer.toHexString((int) s.charAt(i)));
        }
    }

    @Test
    public void ascii2str() throws Exception {
        String s = "\u6d4b\u8bd5";
        System.out.println(s);
    }

    public static double getAngle(double centerX, double centerY, double targetX, double targetY) {
        double angle = Math.toDegrees(Math.atan2(targetY - centerY, targetX - centerX));
        if (angle < 0) angle += 360;
        return angle;
    }
}