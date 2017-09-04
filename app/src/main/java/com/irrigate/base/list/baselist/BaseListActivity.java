package com.irrigate.base.list.baselist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.irrigate.R;
import com.irrigate.base.activity.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/15.
 * <p>
 * 任何带网络请求的页面都可以使用本类快速搭建，对于带列表的页面节省时间尤为明显，
 * 使用者不用再为数据传输，加载逻辑等编写重复代码，只需实现几个变化的部分：
 * 1.接口请求是什么？（Request）
 * 2.接口参数是什么？（Params）
 * 3.接口结果怎么转化？（Result）
 * 4.转化后的结果如何显示？（Adapter）
 */

public abstract class BaseListActivity extends BaseActivity {
    protected Context context;

    private BaseListFragment listFragment;

    private BaseListFragment.OnDataChangedListener onDataChangedListener;

    /**
     * 获取列表的接口请求，{@link BaseTaskPair}的构造方法要求传入你的Task和Result，
     * 注意：它们必须继承自{@link BaseListTask}和{@link BaseListResult}
     *
     * @return
     */
    protected abstract BaseTaskPair getListTask();

    /**
     * 获取列表Adapter
     * <p>
     * 其中的items可以通过{@link BaseListAdapter#getItems()}得到
     * <p>
     * 如果该页面不止一个网络请求，可以通过{@link BaseListAdapter#getResultByTask(Class)}获取对应的结果
     *
     * @return
     */
    protected abstract BaseListAdapter getListAdapter();

    /**
     * 接口参数
     * <p>
     * 直接<code>put</code>接口需要的参数，这里的key将会作为Task中获取的key
     *
     * @param params
     */
    protected abstract void addParams(Map<String, Object> params);

    /**
     * 添加更多的网络请求，它们会以异步的方式同时进行
     *
     * @param extraTasks
     */
    protected void addTask(List<BaseTaskPair> extraTasks) {
        //Empty
    }

    /**
     * 额外的配置信息
     * <p>
     * 包含了空页面样式 加载样式 是否懒加载等等
     *
     * @return
     */
    protected BaseListConfig getConfiguration() {
        return null;
    }

    /**
     * 当显示的数据发生变化，会回调onDataChangedListener
     *
     * @param onDataChangedListener
     */
    public void setOnDataChangedListener(
            BaseListFragment.OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    public final void refresh(boolean showLoading) {
        listFragment.reloadData(showLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Map<String, Object> taskParamMap = new HashMap<>();
        addParams(taskParamMap);
        listFragment = DefaultListFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .commit();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_base_list;
    }

    public static final class DefaultListFragment extends BaseListFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setOnDataChangedListener(((BaseListActivity) getActivity()).getOnDataChangedListener());
        }

        @Override
        protected void addTask(List nonListTasks) {
            ((BaseListActivity) getActivity()).addTask(nonListTasks);
        }

        @Override
        protected BaseTaskPair getListTask() {
            return ((BaseListActivity) getActivity()).getListTask();
        }

        @Override
        protected BaseListConfig getConfig() {
            return ((BaseListActivity) getActivity()).getConfiguration();
        }

        @Override
        protected BaseListAdapter getListAdapter() {
            return ((BaseListActivity) getActivity()).getListAdapter();
        }

        @Override
        protected void addParams(Map params) {
            ((BaseListActivity) getActivity()).addParams(params);
        }

        public static DefaultListFragment newInstance() {
            Bundle args = new Bundle();
            DefaultListFragment fragment = new DefaultListFragment();
            fragment.setArguments(args);
            return fragment;
        }
    }

    private BaseListFragment.OnDataChangedListener getOnDataChangedListener() {
        return onDataChangedListener;
    }
}
