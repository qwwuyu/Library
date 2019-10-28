package com.qwwuyu.lib.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by qiwei on 2019/8/12.
 * 轮播图控件
 */
@SuppressLint("ClickableViewAccessibility")
public class BannerView extends FrameLayout {
    private final int WHAT_LOOP = 100;
    private final ViewPager viewPager;
    private BannerAdapter adapter;
    private VPAdapter vpAdapter;
    private int count;
    private int offset;

    private boolean destroy = true;
    private Handler handler;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewPager = new ViewPager(context);
        viewPager.setOnTouchListener(new ClickListener(viewPager, listener));
        addView(viewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /** 设置适配器 */
    public void setAdapter(BannerAdapter bannerAdapter) {
        if (!destroy) throw new RuntimeException("adapter is not destroy");
        if (bannerAdapter == null) throw new RuntimeException("bannerAdapter can't null");
        if (bannerAdapter.getCount() <= 0) return;
        destroy = false;
        this.adapter = bannerAdapter;
        this.count = adapter.getCount();
        this.offset = 1000 - (1000 % count);
        handler = new Handler(msg -> {
            if (WHAT_LOOP == msg.what) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(msg.what, adapter.loopTime());
            }
            return true;
        });
        vpAdapter = new VPAdapter(adapter, offset);
        viewPager.setAdapter(vpAdapter);
        viewPager.setCurrentItem(offset);
        startLoop();
    }

    /**
     * 设置
     * @param active
     */
    public void setActive(boolean active) {
        if (active) {
            startLoop();
        } else {
            stopLoop();
        }
    }

    public void destroy() {
        if (destroy) return;
        destroy = true;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        viewPager.setAdapter(null);
        viewPager.removeAllViews();
        vpAdapter = null;
        adapter = null;
    }

    public List<View> getAllView() {
        return vpAdapter == null ? Collections.emptyList() : vpAdapter.getViews();
    }

    private void startLoop() {
        if (handler != null && count > 1) {
            handler.removeMessages(WHAT_LOOP);
            handler.sendEmptyMessageDelayed(WHAT_LOOP, adapter.loopTime());
        }
    }

    private void stopLoop() {
        if (handler != null) {
            handler.removeMessages(WHAT_LOOP);
        }
    }

    private static int position2index(BannerAdapter adapter, int offset, int position) {
        int index = (position - offset) % adapter.getCount();
        if (index >= 0) return index;
        else return index + adapter.getCount();
    }

    private static class VPAdapter extends PagerAdapter {
        private final BannerAdapter adapter;
        private final int offset;
        private final int count;
        private SparseArray<List<View>> caches = new SparseArray<>();
        private List<View> views = new ArrayList<>();

        public VPAdapter(BannerAdapter adapter, int offset) {
            this.adapter = adapter;
            this.offset = offset;
            count = adapter.getCount() <= 1 ? adapter.getCount() : Integer.MAX_VALUE;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ViewPager viewPager = (ViewPager) container;
            int index = position2index(adapter, offset, position);
            Integer type = adapter.getType(index);
            List<View> cache = caches.get(type);
            View inflate;
            if (cache == null || cache.isEmpty()) {
                inflate = adapter.createView(container.getContext(), index);
                views.add(inflate);
            } else {
                inflate = cache.remove(0);
            }
            adapter.initView(inflate, index);
            viewPager.addView(inflate);
            return inflate;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View inflate = (View) object;
            ViewPager viewPage = (ViewPager) container;
            viewPage.removeView(inflate);

            int index = position2index(adapter, offset, position);
            Integer type = adapter.getType(index);
            List<View> cache = caches.get(type);
            if (cache == null) {
                cache = new ArrayList<>();
                caches.put(type, cache);
            }
            cache.add(inflate);
        }

        public List<View> getViews() {
            return views;
        }
    }

    private ClickListener.OnClickListener listener = new ClickListener.OnClickListener() {
        @Override
        public void onClick() {
            adapter.onClick(position2index(adapter, offset, viewPager.getCurrentItem()));
        }

        @Override
        public boolean onTouch() {
            if (adapter == null) return false;
            stopLoop();
            return true;
        }

        @Override
        public void onFinish() {
            startLoop();
        }
    };
}
