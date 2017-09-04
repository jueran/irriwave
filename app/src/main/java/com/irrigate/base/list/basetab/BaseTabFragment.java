package com.irrigate.base.list.basetab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.irrigate.base.list.baselist.BaseListAdapter;
import com.irrigate.base.list.baselist.BaseListConfig;
import com.irrigate.base.list.baselist.BaseListFragment;
import com.irrigate.base.list.baselist.BaseTaskPair;

import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/15.
 */
@SuppressWarnings("PMD.GodClass")
public final class BaseTabFragment extends BaseListFragment {
    private static final String ARGS_TAB_FRAGMENT_INDEX = "tab fragment type";

    private int index;
    private BaseTabActivity activity;

    public static BaseListFragment newInstance(int index) {
        BaseListFragment fragment = new BaseTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_TAB_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseTabActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        index = getArguments().getInt(ARGS_TAB_FRAGMENT_INDEX);
        super.onCreate(savedInstanceState);
        setOnDataChangedListener(new OnDataChangedListener() {
            @Override
            public void dataChanged(List curItems, Map otherData) {
                OnFragmentDataChangedListener onFragmentDataChangedListener =
                        activity.getOnFragmentDataChangedListener();
                if (onFragmentDataChangedListener != null) {
                    onFragmentDataChangedListener.dataChangedAt(index, curItems, otherData);
                }
            }
        });
    }

    @Override
    protected void addTask(List nonListTasks) {
        activity.addTask(index, nonListTasks);
    }

    @Override
    protected BaseTaskPair getListTask() {
        return activity.getListTask(index);
    }

    @Override
    protected BaseListAdapter getListAdapter() {
        return activity.getAdapter(index);
    }

    @Override
    protected void addParams(Map params) {
        activity.addParam(index, params);
    }

    @Override
    protected BaseListConfig getConfig() {
        return activity.getConfiguration(index);
    }

    public interface OnFragmentDataChangedListener {
        void dataChangedAt(int index, List curItems, Map otherData);
    }
}
