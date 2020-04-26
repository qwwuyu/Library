package com.qwwuyu.example.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewTreeObserver;

import com.github.chrisbanes.photoview.OnDoubleTapDefaultListener;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.LibMvpActivity;
import com.qwwuyu.lib.mvp.BasePresenter;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.lib.utils.DisplayUtils;
import com.qwwuyu.lib.base.MultipleStateLayout;
import com.qwwuyu.lib.base.TitleView;
import cardview.widget.RoundRectDrawableWithShadow;
import com.qwwuyu.widget.gyro.GyroBean;
import com.qwwuyu.widget.gyro.GyroMarkView;
import com.qwwuyu.widget.gyro.GyroUtil;
import com.qwwuyu.widget.gyro.GyroView;

import androidx.core.view.ViewCompat;

public class GyroActivity extends LibMvpActivity {
    private View shadowView;
    private GyroView gyroView;
    private GyroMarkView markView;
    private PhotoViewAttacher attacher;
    private GyroBean gyroBean = new GyroBean();
    private String map = "H4sIAAAAAAAAAO3TsRGAIBBFQe6IDA0IbUb770mxBQLE2a3g3zwoBQAAAAAAAAAAAAAAAAAAAAAAAPiqreTsCcOyxhGtzp4xLlp77lg+yBURbV++x5nRrd+jvIcs36P/83zMnjEs6w/eVPeDFgAAAAAAAAAAAAAAAAAAAAAAALPdIrgBRkCcAAA=";
    private float[][] lines = new float[][]{{1, 7}, {3, 5}, {3, 5}, {3, 9}, {4, 9}, {4, 3}, {5, 3}, {5, 4f},
            {5.5f, 4.5f}, {5.5f, 4.5f}, {5.5f, 5.5f}, {5.5f, 5.5f}, {6, 6.5f}, {6, 9f}, {7, 10f}, {7, 1}, {7, 2}, {8, 3}, {8, 10},};

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.a_gyro;
    }

    @Override
    protected void init(Bundle bundle, TitleView titleView, MultipleStateLayout stateLayout) {
        shadowView = findViewById(R.id.shadowView);
        gyroView = findViewById(R.id.gyro_gyroView);
        markView = findViewById(R.id.gyro_markView);
        attacher = gyroView.getAttacher();
        attacher.setOnPhotoTapListener(null);
        attacher.clearTouchSlop();
        attacher.setOnDoubleTapListener(new OnDoubleTapDefaultListener(attacher));
        attacher.setScaleLevels(1, 9, 30);
        attacher.setOnMatrixChangeListener(markView);
        gyroView.update(attacher);
        gyroView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                attacher.setScale(attacher.getMediumScale());
                gyroView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        float[][] ls = new float[50 * lines.length][2];
        for (int i = 0; i < lines.length; i++) {
            lines[i][0] = lines[i][0] + 90;
            lines[i][1] = lines[i][1] + 96;
        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < lines.length; j++) {
                ls[i * lines.length + j][0] = lines[j][0] + i;
                ls[i * lines.length + j][1] = lines[j][1] + i;
            }
        }

        gyroBean.setDatas(CommUtil.gzipUnCompress(Base64.decode(map, Base64.DEFAULT)));
        gyroBean.setRobot(new Point(104, 97));
        gyroBean.setLines(lines);
        markView.setData(gyroBean);
        GyroUtil.setEdge(attacher, gyroBean);

//        ViewCompat.setBackground(shadowView, new ShadowDrawableWrapper(
//                this, new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)),
//                DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5)));
        ColorStateList stateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_pressed},
                new int[]{},
        }, new int[]{
                Color.BLACK,
                Color.BLUE,
                Color.RED,
        });
        ViewCompat.setBackground(shadowView, new RoundRectDrawableWithShadow(getResources(), stateList,
                DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5)));
        shadowView.setOnClickListener(v -> shadowView.setEnabled(false));
    }
}