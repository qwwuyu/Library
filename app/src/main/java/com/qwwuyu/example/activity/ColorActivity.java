package com.qwwuyu.example.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.LibMvpActivity;
import com.qwwuyu.lib.base.MvpConfig;
import com.qwwuyu.lib.utils.ToastUtil;
import com.rarepebble.colorpicker.AlphaView;
import com.rarepebble.colorpicker.ColorPickerView;
import com.rarepebble.colorpicker.ObservableColor;
import com.rarepebble.colorpicker.ValueView;

/**
 * Created by qiwei on 2017/8/10
 */
public class ColorActivity extends LibMvpActivity implements ObservableColor.ColorObserver {
    private ColorPickerView pickerView;
    private ValueView valueView;
    private AlphaView alphaView;
    private ImageView resultView;
    private ObservableColor observableColor = new ObservableColor();

    @Override
    protected void initMvpConfig(MvpConfig mvpConfig) {
        mvpConfig.layoutResID(R.layout.a_color);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        pickerView = findViewById(R.id.pickerView);
        valueView = findViewById(R.id.valueView);
        alphaView = findViewById(R.id.alphaView);
        resultView = findViewById(R.id.resultView);
        pickerView.observeColor(observableColor);
        valueView.observeColor(observableColor);
        alphaView.observeColor(observableColor);
        observableColor.addObserver(this);
        observableColor.updateColor(observableColor.getColor(), null);
    }

    public void onClick1(View view) {
        observableColor.updateColor(0x88ff0000, null);
    }

    @Override
    public void updateColor(ObservableColor observableColor, int action) {
        resultView.setImageDrawable(new ColorDrawable(observableColor.getColor()));
        ToastUtil.show(Integer.toHexString(observableColor.getColor()) + " \naction:" + action);
    }
}