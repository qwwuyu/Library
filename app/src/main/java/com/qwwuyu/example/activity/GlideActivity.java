package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qwwuyu.example.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by qiwei on 2017/8/7
 */
public class GlideActivity extends AppCompatActivity {
    private ImageView img;
    private String url = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_glide);
        img = findViewById(R.id.img);
        Glide.with(this).load(url).into(img);
    }
}
