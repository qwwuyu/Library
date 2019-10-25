package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.utils.LogUtils;

/**
 * Created by qiwei on 2017/8/10
 */
public class Test2Activity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test2);
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dialog = new AlertDialog.Builder(this)
                .setPositiveButton("yes", null)
                .setNegativeButton("no", null)
                .create();
        dialog.show();
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(l -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Handler().postDelayed(() -> {
            LogUtils.i("show");
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5000);
    }
}
