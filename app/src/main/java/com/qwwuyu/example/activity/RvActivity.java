package com.qwwuyu.example.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.qwwuyu.example.R;
import com.qwwuyu.lib.adapter.SimpleAdapter;
import com.qwwuyu.lib.adapter.SimpleViewHolder;
import com.qwwuyu.lib.utils.DrawableUtils;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by qiwei on 2019/8/31.
 */
public class RvActivity extends AppCompatActivity {
    private RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_rv);
        rv = findViewById(R.id.rv);
        List<String> list = Arrays.asList("1", "2", "3", "1", "2", "3", "1", "2", "3", "1", "2", "3");
        List<Integer> colors = Arrays.asList(0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff0000, 0xff00ff00, 0xff0000ff, 0xffff0000, 0xff00ff00, 0xff0000ff);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        //manager.setStackFromEnd(true);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new ItemDecorator(-100));
        rv.setAdapter(new SimpleAdapter<String>(this, list, R.layout.item_rv) {
            @Override
            public void onBind(int position, SimpleViewHolder holder, String data) {
                holder.setText(R.id.tv, data);
                ViewCompat.setBackground(holder.getView(R.id.tv), DrawableUtils.getRectangleRadius(colors.get(position), 40));
            }
        });

    }

    public class ItemDecorator extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public ItemDecorator(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position != 0)
                outRect.top = mSpace;
        }
    }
}
