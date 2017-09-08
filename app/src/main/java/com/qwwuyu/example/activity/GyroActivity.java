package com.qwwuyu.example.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.ViewTreeObserver;

import com.github.chrisbanes.photoview.OnDoubleTapDefaultListener;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.BaseActivity;
import com.qwwuyu.lib.utils.CommUtil;
import com.qwwuyu.widget.gyro.GyroBean;
import com.qwwuyu.widget.gyro.GyroMarkView;
import com.qwwuyu.widget.gyro.GyroView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GyroActivity extends BaseActivity {
    @BindView(R.id.gyro_gyroView)
    GyroView gyroView;
    @BindView(R.id.gyro_markView)
    GyroMarkView markView;
    private PhotoViewAttacher attacher;
    private GyroBean gyroBean = new GyroBean();
    private String map = "H4sIAAAAAAAAAO3TsRGAIBBFQe6IDA0IbUb770mxBQLE2a3g3zwoBQAAAAAAAAAAAAAAAAAAAAAAAPiqreTsCcOyxhGtzp4xLlp77lg+yBURbV++x5nRrd+jvIcs36P/83zMnjEs6w/eVPeDFgAAAAAAAAAAAAAAAAAAAAAAALPdIrgBRkCcAAA=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_gyro);
        ButterKnife.bind(this);

        attacher = gyroView.getAttacher();
        attacher.setOnPhotoTapListener(null);
        attacher.clearTouchSlop();
        attacher.setOnDoubleTapListener(new OnDoubleTapDefaultListener(attacher));
        attacher.setScaleLevels(1f, 3f, 9.0f);
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
        gyroBean.setDatas(CommUtil.gzipUnCompress(Base64.decode(map, Base64.DEFAULT)));
        gyroBean.setRobot(new Point(104, 97));
        markView.setData(gyroBean);
    }
}