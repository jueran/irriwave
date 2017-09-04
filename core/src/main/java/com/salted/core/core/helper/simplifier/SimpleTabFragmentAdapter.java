package com.salted.core.core.helper.simplifier;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.salted.core.core.util.LogUtil;

import java.util.List;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-31.
 */

public class SimpleTabFragmentAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private List<Fragment> mFragments;

    public SimpleTabFragmentAdapter(List<Fragment> fragments,
                                    FragmentManager fm, String... tabTitles) {
        super(fm);
        LogUtil.e("hqqp","init SimpleTabFragmentAdapter");
        mFragments = fragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}