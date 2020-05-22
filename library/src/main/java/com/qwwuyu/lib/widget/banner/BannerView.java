package com.qwwuyu.lib.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.qwwuyu.library.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by qiwei on 2019/8/12.
 * 轮播图控件
 */
@SuppressLint("ClickableViewAccessibility")
public class BannerView extends FrameLayout {
    private final int WHAT_LOOP = 100;
    private final int[] GRAVITY_LIST = {Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, Gravity.BOTTOM | Gravity.LEFT, Gravity.BOTTOM | Gravity.RIGHT};

    private final int loopTime;
    private final ViewPager viewPager;
    private RelativeLayout circleGroup;
    private int circleRadius;
    private int circleMargin;
    private Drawable circleEnableDrawable;
    private Drawable circleDisableDrawable;

    private ViewPager.OnPageChangeListener onPageChangeListener;
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

        LayoutParams radiusLayoutParams = null;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerView, defStyleAttr, 0);
        loopTime = a.getInt(R.styleable.BannerView_bvLoopTime, 5000);
        boolean circleEnable = a.getBoolean(R.styleable.BannerView_bvCircleEnable, true);
        if (circleEnable) {
            radiusLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            radiusLayoutParams.gravity = GRAVITY_LIST[a.getInt(R.styleable.BannerView_bvCircleGravity, 0)];
            radiusLayoutParams.bottomMargin = a.getDimensionPixelSize(R.styleable.BannerView_bvCircleMarginBottom, dp2px(5));
            radiusLayoutParams.leftMargin = a.getDimensionPixelSize(R.styleable.BannerView_bvCircleMarginLeft, 0);
            radiusLayoutParams.rightMargin = a.getDimensionPixelSize(R.styleable.BannerView_bvCircleMarginRight, 0);
            circleRadius = a.getDimensionPixelSize(R.styleable.BannerView_bvCircleRadius, dp2px(5));
            circleMargin = a.getDimensionPixelSize(R.styleable.BannerView_bvCircleMargin, dp2px(5));
            circleEnableDrawable = getOval(a.getColor(R.styleable.BannerView_bvCircleEnableColor, 0xffff0000));
            circleDisableDrawable = getOval(a.getColor(R.styleable.BannerView_bvCircleDisableColor, 0xffcccccc));
        }
        a.recycle();

        viewPager = new ViewPager(context);
        viewPager.setOnTouchListener(new ClickListener(viewPager, listener));
        addView(viewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        if (circleEnable) {
            circleGroup = new RelativeLayout(context);
            addView(circleGroup, radiusLayoutParams);
        }
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
                handler.sendEmptyMessageDelayed(msg.what, loopTime);
            }
            return true;
        });
        vpAdapter = new VPAdapter(adapter, offset);
        viewPager.setAdapter(vpAdapter);
        if (circleGroup != null) {
            circleGroup.removeAllViews();
            int count = bannerAdapter.getCount();
            for (int i = 0; i < count; i++) {
                View view = new View(getContext());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(circleRadius, circleRadius);
                lp.leftMargin = i * (circleMargin + circleRadius);
                ViewCompat.setBackground(view, circleDisableDrawable);
                circleGroup.addView(view, lp);
            }
            View enableView = new View(getContext());
            ViewCompat.setBackground(enableView, circleEnableDrawable);
            circleGroup.addView(enableView, new RelativeLayout.LayoutParams(circleRadius, circleRadius));
            if (onPageChangeListener != null) viewPager.removeOnPageChangeListener(onPageChangeListener);
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) enableView.getLayoutParams();
                    float offset = Math.min(count - 1, (position % count + positionOffset));
                    lp.leftMargin = (int) (offset * (circleMargin + circleRadius));
                    enableView.setLayoutParams(lp);
                }
            });
        }
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
            handler.sendEmptyMessageDelayed(WHAT_LOOP, loopTime);
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

    private GradientDrawable getOval(int fillColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setColor(fillColor);
        return gd;
    }

    private int dp2px(float dpValue) {
        return (int) (dpValue * getResources().getDisplayMetrics().density + 0.5f);
    }
}
