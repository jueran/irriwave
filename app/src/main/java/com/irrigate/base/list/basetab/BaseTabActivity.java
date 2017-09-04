package com.irrigate.base.list.basetab;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import com.irrigate.R;
import com.irrigate.base.activity.BaseActivity;
import com.irrigate.base.list.baselist.BaseListAdapter;
import com.irrigate.base.list.baselist.BaseListConfig;
import com.irrigate.base.list.baselist.BaseListFragment;
import com.irrigate.base.list.baselist.BaseTaskPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用本类可以快速实现Tab页,使用和
 * 每一个方法都包含一个<code>tabIndex</code>参数，方便使用者应对不同tab不同需求的情况
 * <p>
 * Created by xinyuanzhong on 2017/6/13.
 */

public abstract class BaseTabActivity extends BaseActivity {
    protected Context context;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected TabListAdapter adapter;

    private BaseTabFragment.OnFragmentDataChangedListener onFragmentDataChangedListener;

    /**
     * 获取全部Tab的标题
     *
     * @return
     */
    protected abstract String[] getTitles();

    /**
     * 获取列表Adapter
     * <p>
     * 其中的items可以通过{@link BaseListAdapter#getItems()}得到
     * <p>
     * 如果该页面不止一个网络请求，可以通过{@link BaseListAdapter#getResultByTask(Class)}获取对应的结果
     *
     * @param tabIndex
     * @return
     */
    protected abstract BaseListAdapter getAdapter(int tabIndex);

    /**
     * * 获取列表的接口请求，{@link BaseTaskPair}的构造方法要求传入你的Task和Result，
     * 注意：它们必须继承自{@link BaseListResult}
     * 和{@link BaseListResult}
     *
     * @param tabIndex
     * @return
     */
    protected abstract BaseTaskPair getListTask(int tabIndex);

    /**
     * 添加更多的网络请求，它们会以异步的方式同时进行
     *
     * @param tabIndex
     * @param extraTasks
     */
    public void addTask(int tabIndex, List<BaseTaskPair> extraTasks) {
        //Empty
    }

    /**
     * 额外的配置信息
     * <p>
     * 包含了空页面样式 加载样式 是否懒加载等等
     *
     * @param index
     * @return
     */
    public BaseListConfig getConfiguration(int index) {
        return null;
    }

    /**
     * 接口参数
     * <p>
     * 直接<code>put</code>接口需要的参数，这里的key将会作为Task中获取的key
     *
     * @param tabIndex
     * @param params
     */
    protected abstract void addParam(int tabIndex, Map<String, Object> params);

    public void refreshAllFragment(boolean showLoading) {
        for (int i = 0; i < adapter.fragments.size(); i++) {
            adapter.refreshFragmentAt(i, showLoading);
        }
    }

    public void refreshFragmentAt(int tabIndex) {
        refreshFragmentAt(tabIndex, true);
    }

    public void refreshFragmentAt(int tabIndex, boolean showLoading) {
        adapter.refreshFragmentAt(tabIndex, showLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        List<BaseListFragment> fragments = new ArrayList<>();
        for (int i = 0; i < getTitles().length; i++) {
            fragments.add(BaseTabFragment.newInstance(i));
        }
        adapter = new TabListAdapter(fragments);
        viewPager.setOffscreenPageLimit(getTitles().length);
        viewPager.setAdapter(adapter);

        if (getConfiguration(0) != null && getConfiguration(0).getTabColorRes() != 0) {
            int tabColor = getResources().getColor(getConfiguration(0).getTabColorRes());
            tabLayout.setSelectedTabIndicatorColor(tabColor);
            tabLayout.setTabTextColors(getResources().getColor(R.color.c2_1), tabColor);
        }
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_base_tab;
    }

    public BaseTabFragment.OnFragmentDataChangedListener getOnFragmentDataChangedListener() {
        return onFragmentDataChangedListener;
    }

    protected void setOnFragmentDataChangedListener(
            BaseTabFragment.OnFragmentDataChangedListener onFragmentDataChangedListener) {
        this.onFragmentDataChangedListener = onFragmentDataChangedListener;
    }

    protected class TabListAdapter extends FragmentPagerAdapter {
        private List<BaseListFragment> fragments;

        public TabListAdapter(List<BaseListFragment> fragments) {
            super(getSupportFragmentManager());
            this.fragments = fragments;
        }

        @Override
        public BaseListFragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTitles()[position];
        }

        public void refreshFragmentAt(int tabIndex, boolean showLoading) {
            BaseListFragment baseListFragment = fragments.get(tabIndex);
            if (baseListFragment != null) {
                baseListFragment.reloadData(showLoading);
            }
        }
    }
}
