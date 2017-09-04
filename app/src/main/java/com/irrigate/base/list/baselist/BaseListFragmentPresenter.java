package com.irrigate.base.list.baselist;

import android.content.Context;

import com.irrigate.R;
import com.irrigate.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/13.
 */

public class BaseListFragmentPresenter implements BaseListFragmentContract.Presenter {
    private Context context;

    private BaseListFragmentContract.Model model;
    private BaseListFragmentContract.View view;

    private boolean hasDataAlready;

    public BaseListFragmentPresenter(Context context,
                                     BaseListFragmentContract.View view,
                                     BaseTaskPair listTask,
                                     List<BaseTaskPair> otherTaskSet,
                                     ParamsProvider paramsProvider) {
        this.context = context;
        this.view = view;
        this.model = new BaseListFragmentModel(context, listTask, otherTaskSet, paramsProvider);
    }

    public final void loadAll(final boolean showLoading) {
        model.loadAll(new BaseListFragmentContract.Model.OnItemsLoadedListener() {
            @Override
            public void onStart() {
                view.hideLoadingMore();
                if (showLoading) view.showLoading(true);
                view.showNetworkView(false);
            }

            @Override
            public void onSuccess(List items, Map<String, Object> result) {
                view.showLoading(false);
                view.showEmpty(false);
                view.showRecyclerView(true);
                view.enableSwipeRefresh(true);
                view.updateData(items, result);
                hasDataAlready = true;
            }

            @Override
            public void onError() {
                view.showLoading(false);
                if (!hasDataAlready) {
                    view.showNetworkView(true);
                    view.enableSwipeRefresh(false);
                    view.showRecyclerView(false);
                }
                ToastUtil.toastShort(context, context.getString(R.string.network_error_tips));
            }

            @Override
            public void onException(Exception e) {
                view.showLoading(false);
                ToastUtil.toastShort(context, e.getMessage());
            }

            @Override
            public void onCancel() {
                view.showLoading(false);
            }

            @Override
            public void onEmpty(Map<String, Object> results) {
                view.showLoading(false);
                view.enableSwipeRefresh(true);
                view.updateData(new ArrayList(), results);
                view.showRecyclerView(!view.shouldTakeOverEmptyStateLogic());
                view.showEmpty(true);
            }
        });
    }

    @Override
    public final void loadMore() {
        if (!model.isPagingEnable()) {
            return;
        }
        model.loadMore(new BaseListFragmentContract.Model.OnItemsLoadedListener() {
            @Override
            public void onStart() {
                view.showLoadingMore();
            }

            @Override
            public void onSuccess(List items, Map<String, Object> result) {
                view.hideLoadingMore();
                view.appendData(items);
            }

            @Override
            public void onError() {
                view.showLoadingMoreError();
            }

            @Override
            public void onException(Exception e) {
                view.hideLoadingMore();
                ToastUtil.toastShort(context, e.getMessage());
            }

            @Override
            public void onCancel() {
                view.hideLoadingMore();
            }

            @Override
            public void onEmpty(Map<String, Object> results) {
                view.showLoadingMoreEmpty();
            }
        });
    }
}
