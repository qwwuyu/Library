package com.qwwuyu.lib.widget.banner;

/**
 * Created by qiwei on 2019/8/12.
 */
public abstract class SBannerAdapter extends BannerAdapter {
    @Override
    public Integer getType(int index) {
        return 0;
    }

    @Override
    public long loopTime() {
        return 5000;
    }
}
