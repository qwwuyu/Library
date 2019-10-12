package com.qwwuyu.example.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qwwuyu.example.R;
import com.qwwuyu.widget.LinkageView;

import java.util.Arrays;

/**
 * Created by qiwei on 2017/8/10
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_test);
        LinkageView linkageView = findViewById(R.id.linkageView);
        linkageView.setAdapter(new LinkageView.LinkageAdapter() {
            @Override
            public String[] getHintTitle() {
                return new String[]{"1", "2", "3"};
            }

            @Override
            public void getLinkageData(LinkageView.LinkageData[] selects, int position, CallBack callBack) {
                callBack.call(-1, Arrays.asList(() -> "1", () -> "2", () -> "3"));
            }

            @Override
            public void onSelect(LinkageView.LinkageData[] selects) {

            }

            @NonNull
            @Override
            public LinkageView.RvAdapter.RvViewHolder onCreateViewHolder(Context context, @NonNull ViewGroup parent, int viewType) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false);
                return new LinkageView.RvAdapter.RvViewHolder(inflate) {
                    @Override
                    public TextView getTextView() {
                        return inflate.findViewById(R.id.tv);
                    }
                };
            }

            @Override
            public int[] layouts() {
                return new int[]{R.layout.item_rv, R.layout.item_rv, R.layout.item_rv};
            }

            @Override
            public int[] retryIds() {
                return new int[]{R.id.tv, R.id.tv, R.id.tv};
            }
        });
    }

    public void onClick1(View view) {
    }
}