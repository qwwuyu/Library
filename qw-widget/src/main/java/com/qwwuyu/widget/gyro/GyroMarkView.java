package com.qwwuyu.widget.gyro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.qwwuyu.widget.R;

import java.nio.IntBuffer;


public class GyroMarkView extends View implements OnMatrixChangedListener {
    private final int side = 200, alpha = 102;
    private Paint robotPaint = new Paint(), mapPaint = new Paint(), linePaint = new Paint();
    private Point robot, charge;
    private PointF fRobot = new PointF(), fCharge = new PointF();
    private Bitmap chargeBitmap, mapBitmap;
    private RectF rectF;
    private float robotDs, changeDs;
    private long diff = 2000;
    private Matrix mMatrix = new Matrix();
    private float[] fm = new float[9];

    private int[] is = new int[side * side];
    private int[] colors = GyroUtil.getColors();

    public GyroMarkView(Context context) {
        super(context);
        init();
    }

    public GyroMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GyroMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        robotDs = 4f * getResources().getDisplayMetrics().density;//
        changeDs = 9f * getResources().getDisplayMetrics().density;//
        robotPaint.setColor(0xffff0000);
        robotPaint.setAntiAlias(true);
        robotPaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(0xffcccccc);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        chargeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_gyro_charge);
        mapBitmap = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888);
    }

    public void setData(GyroBean gyroBean) {
        if (gyroBean == null || gyroBean.getDatas() == null || gyroBean.getDatas().length != side * side) return;
        byte[] bs = gyroBean.getDatas();
        robot = gyroBean.getRobot();
        if (robot != null && robot.x < 0) robot = null;
        charge = null;
        int areaNum = 0;
        for (int i = 0; i < bs.length; i++) {
            is[i] = colors[bs[i] & 0b01111111];
            if (charge == null && (bs[i] & 0b1000000) == 0b1000000) charge = new Point(i % side, i / side);
            if ((bs[i] & 0b00001001) != 0) areaNum++;
        }
        gyroBean.setAreaNum(areaNum);
        mapBitmap.copyPixelsFromBuffer(IntBuffer.wrap(is));
        scale();
    }

    @Override
    public void onMatrixChanged(RectF rect, Matrix matrix) {
        if (rectF == null) rectF = new RectF();
        rectF.set(rect);
        if (matrix != null && matrix.isIdentity()) {
            matrix = null;
        }
        if (matrix == null && !mMatrix.isIdentity() || matrix != null && !mMatrix.equals(matrix)) {
            mMatrix.set(matrix);
            mMatrix.getValues(fm);
        }
        scale();
    }

    private void scale() {
        if (robot != null && rectF != null) {
            GyroUtil.changePoint(rectF, side, side, robot, fRobot, 0.5f, 0.5f);
        }
        if (charge != null && rectF != null) {
            GyroUtil.changePoint(rectF, side, side, charge, fCharge, 0.5f, 0.5f);
            fCharge.set(fCharge.x, fCharge.y - changeDs);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF == null || (robot == null && charge == null)) return;
        canvas.save();
        canvas.concat(mMatrix);
        canvas.drawBitmap(mapBitmap, 0, 0, mapPaint);
        canvas.restore();
        drawLine(canvas);
        if (charge != null) {
            drawCharge(canvas, fCharge);
        }
        if (robot != null) {
            drawWater2(canvas, fRobot, 0xff00ffff);
            drawRobot(canvas, fRobot, 0xff00ffff);
            invalidate();
        }
    }

    private void drawLine(Canvas canvas) {
        float scale = fm[Matrix.MSCALE_X];
        linePaint.setStrokeWidth(scale / 100);
        int w = getWidth(), h = getHeight();
        for (float i = (fm[Matrix.MTRANS_X] % scale + scale) % scale; i <= w; i += scale) {
            canvas.drawLine(i, 0, i, h, linePaint);
        }
        for (float i = (fm[Matrix.MTRANS_Y] % scale + scale) % scale; i <= h; i += scale) {
            canvas.drawLine(0, i, w, i, linePaint);
        }
    }

    /** 0~2000ms => 0~1f */
    private float rate(long diff) {
        return (float) (System.currentTimeMillis() % diff) / diff;
    }

    /** 0~2000ms => 1~0f~1f */
    private float rateCenter(long diff) {
        return Math.abs(1f - 2 * rate(diff));
    }

    /** 画充电桩 */
    private void drawCharge(Canvas canvas, PointF charge) {
        robotPaint.setAlpha(255);
        canvas.drawBitmap(chargeBitmap, charge.x - chargeBitmap.getWidth() / 2, charge.y - chargeBitmap.getHeight() / 2, robotPaint);
    }

    /** 画机器人 */
    private void drawRobot(Canvas canvas, PointF robot, int color) {
        robotPaint.setColor(color);
        canvas.drawCircle(robot.x, robot.y, robotDs, robotPaint);
    }

    /** 画水波纹,收回 */
    private void drawWater2(Canvas canvas, PointF robot, int color) {
        float rate = rateCenter(diff);
        robotPaint.setColor(color);
        robotPaint.setAlpha(alpha);
        canvas.drawCircle(robot.x, robot.y, robotDs + robotDs * rate, robotPaint);
    }
}