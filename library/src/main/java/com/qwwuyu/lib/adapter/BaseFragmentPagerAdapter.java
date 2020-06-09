package com.qwwuyu.lib.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    private List<Fragment> fragments;

    public BaseFragmentPagerAdapter(FragmentManager fm, int behavior, List<String> titles, List<Fragment> fragments) {
        super(fm, behavior);
        this.titles = titles;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? null : titles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
