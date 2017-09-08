package com.qwwuyu.lib.stadyTest;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest {
    @Test
    public void test() throws Exception {
        System.out.println(getAngle(0, 0, 1, 1));
        System.out.println(getAngle(0, 0, -1, 1));
        System.out.println(getAngle(0, 0, 1, -1));
        System.out.println(getAngle(0, 0, -1, -1));

    }

    public static double getAngle(double centerX, double centerY, double targetX, double targetY) {
        double angle = Math.toDegrees(Math.atan2(targetY - centerY, targetX - centerX));
        if (angle < 0) angle += 360;
        return angle;
    }
}