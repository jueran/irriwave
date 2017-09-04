package com.salted.core.core.widget.loadmorerecyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by xinyuanzhong on 2017/7/19.
 */

public final class RecyclerViewUtils {
    private RecyclerViewUtils() {
    }

    public static boolean isFooter(int position, RecyclerView parent) {
        return position > parent.getAdapter().getItemCount() - getFooterCount(parent) - 1;
    }

    /**
     * 是否是非Footer的最后一条数据
     *
     * @param position
     * @param parent
     * @return
     */
    public static boolean isLastItem(int position, RecyclerView parent) {
        return position == parent.getAdapter().getItemCount() - getFooterCount(parent) - 1;
    }

    public static int getFooterCount(RecyclerView parent) {
        if (parent.getAdapter() instanceof HeaderAndFooterRecyclerViewAdapter) {
            return ((HeaderAndFooterRecyclerViewAdapter) parent.getAdapter()).getFooterViewsCount();
        }
        return 0;
    }

    public static int getHeaderCount(RecyclerView parent) {
        if (parent.getAdapter() instanceof HeaderAndFooterRecyclerViewAdapter) {
            return ((HeaderAndFooterRecyclerViewAdapter) parent.getAdapter()).getHeaderViewsCount();
        }
        return 0;
    }
}
