package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.base.BaseActivity;

/**
 * Created by qiwei on 2017/8/10
 */
public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test);
    }

    public void onClick1(View view) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setPositiveButton("yes", null)
                .setNegativeButton("no", null)
                .create();
        dialog.show();
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
