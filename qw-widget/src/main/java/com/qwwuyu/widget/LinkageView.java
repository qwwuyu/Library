package com.qwwuyu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiwei on 2018/3/30 9:51
 * Description 仿淘宝地址地区选择.
 */
public class LinkageView<T extends LinkageView.LinkageData> extends LinearLayout {
    /** 指示器适配器 */
    private final CommonNavigatorAdapter navigatorAdapter;
    /** 内容vp */
    private final ViewPager vp;
    /** 内容适配器 */
    private final LinkagePagerAdapter pagerAdapter;
    private LinkageAdapter adapter;
    /** 选择的title */
    private LinkageData[] titles;

    public LinkageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinkageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinkageView);
        MagicIndicator magicIndicator = new MagicIndicator(context);
        addView(magicIndicator, new LayoutParams(LayoutParams.MATCH_PARENT, a.getDimensionPixelSize(R.styleable.LinkageView_linkageIndicatorHeight, 0)));
        View line = new View(context);
        line.setBackgroundColor(a.getColor(R.styleable.LinkageView_linkageLineColor, 0));
        addView(line, new LayoutParams(LayoutParams.MATCH_PARENT, a.getDimensionPixelSize(R.styleable.LinkageView_linkageLineSpace, 0)));
        vp = new ViewPager(context);
        addView(vp, new LayoutParams(LayoutParams.MATCH_PARENT, a.getDimensionPixelSize(R.styleable.LinkageView_linkageVpHeight, 0)));
        a.recycle();

        pagerAdapter = new LinkagePagerAdapter(getContext(), vp);
        vp.setAdapter(pagerAdapter);

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        navigatorAdapter = new LinkageNavigatorAdapter(new OnTitleClickListener() {
            @Override
            public void onTitleClick(int position) {
                if (position == 0 || titles[position - 1] != null) {
                    vp.setCurrentItem(position);
                }
            }
        });
        commonNavigator.setAdapter(navigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, vp);
    }

    public void setAdapter(@NonNull LinkageAdapter adapter) {
        this.adapter = adapter;
        pagerAdapter.setCount(0);
        titles = new LinkageData[adapter.getCount()];

        pagerAdapter.setCount(1);
        navigatorAdapter.notifyDataSetChanged();
    }

    public static abstract class LinkageAdapter {
        private final String[] hintTitle;

        public LinkageAdapter() {
            hintTitle = getHintTitle();
        }

        /** 获取选择前的标题 */
        public abstract String[] getHintTitle();

        /** 视图数 */
        public int getCount() {
            return hintTitle.length;
        }

        /** 获取数据 */
        public abstract void getLinkageData(LinkageData[] selects, int position, CallBack callBack);

        /** 选择完毕 */
        public abstract void onSelect(LinkageData[] selects);

        /** 列表Item */
        @NonNull
        public abstract RvAdapter.RvViewHolder onCreateViewHolder(Context context, @NonNull ViewGroup parent, int viewType);

        /** 加载中(index=0) 网络失败等视图 */
        public abstract int[] layouts();

        /** 重新请求的id */
        public abstract int[] retryIds();

        /** 获取MagicIndicator的标题 */
        protected IPagerTitleView getTitleView(Context context, final int index, final OnTitleClickListener listener, String text) {
            SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
            simplePagerTitleView.setNormalColor(0xff9ea3a6);
            simplePagerTitleView.setSelectedColor(0xfff03059);
            simplePagerTitleView.setText(text);
            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTitleClick(index);
                }
            });
            return simplePagerTitleView;
        }

        /** 获取MagicIndicator的指示器 */
        protected IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
            linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
            linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 20));
            linePagerIndicator.setColors(0xffe33553);
            return linePagerIndicator;
        }

        public interface CallBack {
            /**
             * @param state       显示第几个视图,显示内容-1,
             * @param linkageData 返回的数据
             */
            void call(int state, List<? extends LinkageData> linkageData);
        }
    }

    /** 指示器适配器 */
    private class LinkageNavigatorAdapter extends CommonNavigatorAdapter {
        private OnTitleClickListener listener;

        public LinkageNavigatorAdapter(OnTitleClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return adapter == null ? 0 : adapter.getCount();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            if (adapter == null) return null;
            return adapter.getTitleView(context, index, listener,
                    titles[index] != null ? titles[index].getText() : adapter.getHintTitle()[index]);
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            return adapter != null ? adapter.getIndicator(context) : null;
        }
    }

    public interface OnTitleClickListener {
        void onTitleClick(int position);
    }

    /** viewpagerAdapter */
    private class LinkagePagerAdapter extends PagerAdapter {
        private final Context context;
        private final ViewPager vp;
        private int count;

        public LinkagePagerAdapter(Context context, ViewPager vp) {
            this.vp = vp;
            this.context = context;
        }

        /** 设置保留页面数目 */
        public void setCount(int count) {
            this.count = count;
            notifyDataSetChanged();
            vp.setOffscreenPageLimit(count);
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
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            final FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setTag(position);
            final RecyclerView rv = new RecyclerView(context);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(context));
            //处理列表点击返回事件
            final RvAdapter rvAdapter = new RvAdapter(context, adapter, new RvAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int index, LinkageData linkageData, boolean change) {
                    if (change) {//如果改变了,重置选择和titles
                        titles[position] = linkageData;
                        for (int i = position + 1; i < titles.length; i++) {
                            titles[i] = null;
                        }
                        navigatorAdapter.notifyDataSetChanged();
                    }
                    if (position + 2 > adapter.getCount()) {//最后一页,选择完毕
                        adapter.onSelect(titles);
                    } else if (change) {//不是最后一页,清理下一页并重新加载
                        setCount(position + 1);
                        setCount(position + 2);
                    }
                    vp.setCurrentItem(position + 1);//跳转到下一页
                }
            });
            rv.setAdapter(rvAdapter);
            //加载视图
            final int[] layouts = adapter.layouts();
            final int[] ids = adapter.retryIds();
            for (int i = 0; i < layouts.length; i++) {
                View inflate = View.inflate(context, layouts[i], frameLayout);
                if (ids[i] != 0 && inflate.findViewById(ids[i]) != null) {
                    inflate.findViewById(ids[i]).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getLinkageData(position, frameLayout, layouts, rv, rvAdapter);
                        }
                    });
                }
            }
            frameLayout.addView(rv, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            getLinkageData(position, frameLayout, layouts, rv, rvAdapter);
            container.addView(frameLayout);
            return frameLayout;
        }

        /** 获取数据并展示 */
        private void getLinkageData(int position, final FrameLayout frameLayout, final int[] layouts, final RecyclerView rv, final RvAdapter rvAdapter) {
            frameLayout.getChildAt(0).setVisibility(VISIBLE);
            for (int i = 1; i < frameLayout.getChildCount(); i++) {
                View child = frameLayout.getChildAt(i);
                child.setVisibility(GONE);
            }
            adapter.getLinkageData(titles, position, new LinkageAdapter.CallBack() {
                @Override
                public void call(int state, List<? extends LinkageData> linkageData) {
                    for (int i = 0; i < frameLayout.getChildCount(); i++) {
                        View child = frameLayout.getChildAt(i);
                        child.setVisibility(GONE);
                    }
                    if (0 <= state && state < layouts.length) {
                        frameLayout.getChildAt(state).setVisibility(VISIBLE);
                    } else {
                        rv.setVisibility(VISIBLE);
                    }
                    rvAdapter.setLinkageData(linkageData);
                    if (linkageData != null && linkageData.isEmpty()) {
                        adapter.onSelect(titles);
                    }
                }
            });
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return (int) ((View) object).getTag() < count ? POSITION_UNCHANGED : POSITION_NONE;
        }
    }

    /** 列表adapter */
    public static class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {
        private final Context context;
        private final LinkageAdapter adapter;
        private final OnItemClickListener listener;

        private List<LinkageData> linkageData = new ArrayList<>();
        private int select = -1;

        public RvAdapter(Context context, LinkageAdapter adapter, OnItemClickListener listener) {
            this.context = context;
            this.adapter = adapter;
            this.listener = listener;
        }

        public void setLinkageData(List<? extends LinkageData> linkageData) {
            this.linkageData.clear();
            if (linkageData != null) this.linkageData.addAll(linkageData);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RvAdapter.RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return adapter.onCreateViewHolder(context, parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RvAdapter.RvViewHolder holder, int p) {
            final int position = holder.getAdapterPosition();
            final LinkageData data = linkageData.get(position);
            holder.getTextView().setText(data.getText());
            holder.getTextView().setTextColor(select == position ? 0xfff03059 : 0xff141414);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (select == position) {
                        listener.onItemClick(position, data, false);
                    } else {
                        int lastSelect = select;
                        select = position;
                        notifyItemChanged(lastSelect);
                        notifyItemChanged(select);
                        listener.onItemClick(position, data, true);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return linkageData.size();
        }

        public interface OnItemClickListener {
            /**
             * @param position    点击的位子
             * @param linkageData 点击的LinkageData
             * @param change      是否改变点击条目
             */
            void onItemClick(int position, LinkageData linkageData, boolean change);
        }

        public abstract static class RvViewHolder extends RecyclerView.ViewHolder {
            public RvViewHolder(View itemView) {
                super(itemView);
            }

            public abstract TextView getTextView();
        }
    }

    /** 列表数据 */
    public interface LinkageData {
        String getText();
    }
}
