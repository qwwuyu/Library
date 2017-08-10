package com.qwwuyu.example.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qwwuyu.example.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qiwei on 2017/8/7
 */
public class GlideActivity extends AppCompatActivity {
    @BindView(R.id.img)
    ImageView img;
    private String url = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_glide);
        ButterKnife.bind(this);
        Glide.with(this).load(url).into(img);
    }
}
