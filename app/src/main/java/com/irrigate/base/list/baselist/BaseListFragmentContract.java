package com.irrigate.base.list.baselist;

import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/13.
 */

public interface BaseListFragmentContract {
    interface View<T> {
        void showNetworkView(boolean show);

        void showLoading(boolean show);

        void showEmpty(boolean show);

        void showLoadingMore();

        void showLoadingMoreError();

        void showLoadingMoreEmpty();

        void hideLoadingMore();

        void updateData(List<T> items, Map<String, Object> result);

        void appendData(List<T> moreItems);

        void enableSwipeRefresh(boolean enable);

        void showRecyclerView(boolean show);

        boolean shouldTakeOverEmptyStateLogic();
    }

    interface Presenter {
        void loadAll(boolean showLoading);

        void loadMore();
    }

    interface Model {
        boolean isPagingEnable();

        interface OnItemsLoadedListener {
            void onStart();

            void onSuccess(List items, Map<String, Object> result);

            void onError();

            void onException(Exception e);

            void onCancel();

            void onEmpty(Map<String, Object> results);
        }

        void loadAll(OnItemsLoadedListener onItemsLoadedListener);

        void loadMore(OnItemsLoadedListener onMoreLoadedListener);
    }

}
