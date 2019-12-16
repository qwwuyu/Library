package com.qwwuyu.widget.gyro;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;

import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class GyroUtil {
    /** 机器人 紫色 */
    private static final int ROBOT = 0xffff0099;//0b0001000
    /** 充电 橙色 */
    private static final int CHARGE = 0xff00CCff;//0b1000000
    /** 异常点 红色 */
    private static final int UNUSUAL = 0xff0000ff;//0b0000100
    /** 超出范围 深绿色 */
    private static final int BEYOND = 0xff88ff00;//0b0100000
    /** 可达障碍物 蓝色 */
    private static final int REACHABLE = 0xffff8800;//0b0000011
    /** 不可达障碍物 深蓝色 */
    private static final int UNREACHABLE = 0xffff0000;//0b0000010
    /** 已经覆盖 浅绿色 */
    private static final int COVER = 0xff00ff88;//0b0000001
    /** 未收到数据 白色 */
    private static final int NON = 0x0;//0b0000000

    private GyroUtil() {
    }

    public static int[] getColors() {
        int[] colors = new int[128];
        for (int i = 0; i < colors.length; i++) {
            if ((i & 0b1000000) == 0b1000000) colors[i] = CHARGE;
            else if ((i & 0b0001000) == 0b0001000) colors[i] = ROBOT;
            else if ((i & 0b0000100) == 0b0000100) colors[i] = UNUSUAL;
            else if ((i & 0b0100000) == 0b0100000) colors[i] = BEYOND;
            else if ((i & 0b0000011) == 0b0000011) colors[i] = REACHABLE;
            else if ((i & 0b0000010) == 0b0000010) colors[i] = UNREACHABLE;
            else if ((i & 0b0000001) == 0b0000001) colors[i] = COVER;
            else colors[i] = NON;
        }
        return colors;
    }

    public static void scalePoint(RectF rect, int width, int height, Point point, PointF fCharge, float scale) {
        float percentX = (float) point.x / width + 0.5f / width;
        float percentY = (float) point.y / height + 0.5f / height;
        fCharge.set((rect.left + percentX * rect.width()) / scale, (rect.top + percentY * rect.height()) / scale);
    }

    public static void changePoint(RectF rect, int width, int height, Point point, PointF fCharge, float offsetX, float offsetY) {
        float percentX = (float) point.x / width + offsetX / width;
        float percentY = (float) point.y / height + offsetY / height;
        fCharge.set(rect.left + percentX * rect.width(), rect.top + percentY * rect.height());
    }

    /**
     * 从矩阵中获取有值矩阵
     *
     * @param bs  矩阵
     * @param w   宽度
     * @param def 空值
     * @return 有值矩阵
     */
    @Nullable
    public static Rect getExistRect(byte[] bs, int w, byte def) {
        Rect rect = new Rect(-1, -1, -1, -1);
        int h = bs.length / w;
        for (int i = 0; i < bs.length; i++) {
            if (bs[i] != def) {
                rect.top = i / w;
                break;
            }
        }
        if (rect.top == -1) return null;
        loop:
        for (int i = 0; i < w; i++) {
            for (int j = rect.top * w + i; j < bs.length; j += w) {
                if (bs[j] != def) {
                    rect.left = j % w;
                    break loop;
                }
            }
        }
        loop:
        for (int i = h - 1; i >= 0; i--) {
            for (int j = i * w + rect.left, max = i * w + w; j < max; j++) {
                if (bs[j] != def) {
                    rect.bottom = j / w;
                    break loop;
                }
            }
        }
        loop:
        for (int i = w - 1; i >= 0; i--) {
            for (int j = rect.bottom * w + i, max = rect.top * w + i; j >= max; j -= w) {
                if (bs[j] != def) {
                    rect.right = j % w;
                    break loop;
                }
            }
        }
        return rect;
    }

    /** 获取中心对称的存在矩阵 */
    public static Rect getCenterRect(Rect rect, int w, int h) {
        w = w - 1;
        h = h - 1;
        Rect centerRect = new Rect(rect);
        if (w - centerRect.left > centerRect.right) centerRect.right = w - centerRect.left;
        else centerRect.left = w - centerRect.right;
        if (h - centerRect.top > centerRect.bottom) centerRect.bottom = h - centerRect.top;
        else centerRect.top = h - centerRect.bottom;
        return centerRect;
    }

    /** 获取rect的数据 */
    public static byte[] copyRect(byte[] bs, int w, Rect rect) {
        int bw = rect.right - rect.left + 1, bh = rect.bottom - rect.top + 1;
        byte[] bytes = new byte[bw * bh];
        for (int i = 0; i < bh; i++) {
            int i1 = i * bw, i2 = (rect.top + i) * w + rect.left;
            System.arraycopy(bs, i2, bytes, i1, bw);
        }
        return bytes;
    }

    public static void setEdge(PhotoViewAttacher attacher, GyroBean bean) {
//        if (bean == null || bean.getRobot() == null || bean.getRobot().x < 0) return;
//        Point robot = bean.getRobot();
//        attacher.setEdge(robot.x / 200f, robot.y / 200f, (robot.x + 1) / 200f, (robot.y + 1) / 200f);
        if (bean == null || bean.getDatas() == null || bean.getDatas().length != 40000) return;
        Rect existRect = GyroUtil.getExistRect(bean.getDatas(), 200, (byte) 0);
        if (existRect == null) {
            attacher.setEdge(1, 1, 0, 0);
        } else {
            attacher.setEdge(existRect.left / 200f, existRect.top / 200f, (existRect.right + 1) / 200f, (existRect.bottom + 1) / 200f);
        }
    }
}
