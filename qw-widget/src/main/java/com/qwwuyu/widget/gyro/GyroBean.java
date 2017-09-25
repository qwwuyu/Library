package com.qwwuyu.widget.gyro;

import android.graphics.Point;

public class GyroBean {
    private byte[] datas;
    private Point robot;
    private int areaNum;
    private int time;
    private float[][] lines;

    public Point getRobot() {
        return robot;
    }

    public void setRobot(Point robot) {
        this.robot = robot;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    public int getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(int areaNum) {
        this.areaNum = areaNum;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public float[][] getLines() {
        return lines;
    }

    public void setLines(float[][] lines) {
        this.lines = lines;
    }
}
