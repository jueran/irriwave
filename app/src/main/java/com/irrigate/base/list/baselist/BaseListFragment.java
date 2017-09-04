package com.irrigate.base.list.baselist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.irrigate.R;
import com.irrigate.base.activity.BaseActivity;
import com.irrigate.core.helper.activityhelper.ViewHelper;
import com.irrigate.core.util.UIUtils;
import com.irrigate.core.widget.loadmorerecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.irrigate.core.widget.loadmorerecyclerview.LoadMoreRecyclerView;
import com.irrigate.core.widget.loadmorerecyclerview.OnLoadMoreEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/13.
 */
public abstract class BaseListFragment<T> extends Fragment
        implements BaseListFragmentContract.View<T> {

    public static final int DELAY_SHORT = 50;
    public static final int DELAY_LONG = 200;
    public static final int SCROLL_TO_TOP_BTN_MARGIN_BOTTOM = 30;
    public static final int SCROLL_TO_TOP_BTN_MARGIN_RIGHT = 15;
    protected Context context;
    private SwipeRefreshLayout refreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private ViewGroup fragmentLayout;
    private View errorView;
    private View reloadBtn;
    private View emptyView;
    private View scrollToTopBtn;

    private Map<String, Object> params = new HashMap<>();

    private BaseListFragmentPresenter presenter;

    private BaseListConfig config = new BaseListConfig();

    protected BaseListAdapter adapter;

    private boolean isViewCreated;
    private boolean isVisibleFirstTime = true;

    private boolean isScrollToTopBtnVisible;

    private OnDataChangedListener onDataChangedListener;

    protected BaseListConfig getConfig() {
        return new BaseListConfig();
    }

    protected void addTask(List<BaseTaskPair> extraTasks) {
        //Empty
    }

    protected abstract BaseTaskPair getListTask();

    protected abstract BaseListAdapter getListAdapter();

    protected abstract void addParams(Map<String, Object> params);

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = getConfig() == null ? new BaseListConfig() : getConfig();

        List<BaseTaskPair> nonListTasks = new ArrayList<>();
        addTask(nonListTasks);
        presenter = new BaseListFragmentPresenter(getContext(), this,
                getListTask(), nonListTasks, getParamsProvider());
    }

    private ParamsProvider getParamsProvider() {
        return new ParamsProvider() {
            @Override
            public Map<String, Object> getParams() {
                params.clear();
                addParams(params);
                return params;
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_base_list, null);
        fragmentLayout = (ViewGroup) refreshLayout.findViewById(R.id.content_container);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setEnabled(!config.isDisableSwipeRefresh());
        adapter = getListAdapter();

        recyclerView = (LoadMoreRecyclerView) fragmentLayout
                .findViewById(R.id.load_more_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setOnLoadMoreEvent(new OnLoadMoreEvent() {
            @Override
            public void loadMore() {
                presenter.loadMore();
            }
        });
        if (config.getItemDivider() != null) {
            recyclerView.addItemDecoration(config.getItemDivider());
        }
        recyclerView.setShowBottomExtraSpace(true);
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(adapter));
        recyclerView.setVisibility(config.isShowListWhenFirstIn() ? View.VISIBLE : View.GONE);
        if (!config.isDisableScrollToTop()) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy == 0 && dx == 0) {
                        return;
                    }
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).
                                findFirstVisibleItemPosition();
                        showScrollToTopBtn(firstVisibleItemPosition > 0);
                    }
                }
            });
        }
        isViewCreated = true;
        return refreshLayout;
    }

    private void showScrollToTopBtn(final boolean show) {
        if (show ^ isScrollToTopBtnVisible) {
            if (scrollToTopBtn == null) {
                createScrollToTopBtn();
            }
            Animation animation = AnimationUtils.loadAnimation(getContext(), show
                    ? R.anim.anim_fab_button_show
                    : R.anim.anim_fab_button_dismiss);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //empty
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    scrollToTopBtn.setVisibility(show ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    //empty
                }
            });
            scrollToTopBtn.startAnimation(animation);
            isScrollToTopBtnVisible = show;
        }
    }

    private void createScrollToTopBtn() {
        scrollToTopBtn = new ImageView(getContext());
        scrollToTopBtn.setBackgroundResource(R.drawable.ic_scroll_to_top);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.bottomMargin = UIUtils.dp2px(getContext(), SCROLL_TO_TOP_BTN_MARGIN_BOTTOM);
        params.rightMargin = UIUtils.dp2px(getContext(), SCROLL_TO_TOP_BTN_MARGIN_RIGHT);
        scrollToTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
                showScrollToTopBtn(false);
            }
        });

        fragmentLayout.addView(scrollToTopBtn, params);
    }

    private final void loadData() {
        reloadData(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (config.isLazyMode() && isVisibleToUser && isViewCreated && isVisibleFirstTime) {
            loadData();
            isVisibleFirstTime = false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (config.isLazyMode() && !getUserVisibleHint()) {
            return;
        }
        loadData();
        isVisibleFirstTime = false;
    }

    @Override
    public void showNetworkView(boolean show) {
        if (show && errorView == null) {
            ViewStub errorViewStub = (ViewStub) fragmentLayout.findViewById(R.id.error_view_stub);
            errorView = errorViewStub.inflate();

            reloadBtn = fragmentLayout.findViewById(R.id.reload_btn);
            reloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
        }
        if (errorView != null) {
            errorView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        showScrollToTopBtn(false);
    }

    @Override
    public void showLoading(final boolean show) {
        if (config.isUseDialogLoading()) {
            if (getActivity() instanceof BaseActivity) {
                ViewHelper viewHelper = ((BaseActivity) getActivity()).getViewHelper();
                if (show) {
                    viewHelper.showLoadingView();
                } else {
                    viewHelper.hideLoadingView();
                }
            }
        } else {
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(show);
                }
            }, show ? DELAY_SHORT : DELAY_LONG);
        }
    }

    @Override
    public boolean shouldTakeOverEmptyStateLogic() {
        return config.isAutoControlEmptyView();
    }

    @Override
    public void showEmpty(boolean show) {
        showScrollToTopBtn(false);

        if (!config.isAutoControlEmptyView()) {
            return;
        }
        if (show && emptyView == null) {
            ViewStub emptyViewStub = (ViewStub) fragmentLayout.findViewById(R.id.empty_view_stub);
            if (config.getCustomEmptyLayoutRes() != 0) {
                emptyViewStub.setLayoutResource(config.getCustomEmptyLayoutRes());
                emptyView = emptyViewStub.inflate();
            } else {
                emptyView = emptyViewStub.inflate();
                configEmptyView();
            }
        }
        if (emptyView != null) {
            emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void configEmptyView() {
        if (!TextUtils.isEmpty(config.getEmptyTip())) {
            TextView emptyTips = (TextView) emptyView.findViewById(R.id.no_data_text);
            emptyTips.setText(config.getEmptyTip());
        }

        if (!TextUtils.isEmpty(config.getEmptyBtnText())) {
            TextView emptyBtnText = (TextView) emptyView.findViewById(R.id.no_data_btn_text);
            emptyBtnText.setVisibility(View.VISIBLE);
            emptyBtnText.setText(config.getEmptyBtnText());

            if (config.getEmptyBtnClickListener() != null) {
                emptyBtnText.setOnClickListener(config.getEmptyBtnClickListener());
            }
        }

        int emptyPicRes = config.getEmptyPicRes();
        if (emptyPicRes != 0) {
            Drawable drawable = getResources().getDrawable(emptyPicRes);
            if (drawable != null) {
                ImageView emptyImageView = (ImageView) emptyView.
                        findViewById(R.id.no_data_image);
                emptyImageView.setImageDrawable(drawable);
            }
        }
    }

    @Override
    public void showRecyclerView(boolean show) {
        recyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void enableSwipeRefresh(boolean enable) {
        refreshLayout.setEnabled(enable && !config.isDisableSwipeRefresh());
    }

    @Override
    public void showLoadingMore() {
        recyclerView.setLoadMoreStateLoading();
    }

    @Override
    public void showLoadingMoreError() {
        recyclerView.setLoadMoreResultNetworkError(null);
    }

    @Override
    public void showLoadingMoreEmpty() {
        recyclerView.setLoadMoreResultNoMoreData();
    }

    @Override
    public void hideLoadingMore() {
        recyclerView.setLoadMoreResultCompleted();
    }

    @Override
    public void updateData(List<T> items, Map<String, Object> result) {
        adapter.setDataMap(result);
        adapter.setItems(items);
        adapter.notifyDataSetChanged();

        notifyOutSideDataChanged(adapter.getItems(), adapter.getDataMap());
    }

    @Override
    public void appendData(List<T> moreItems) {
        adapter.items.addAll(moreItems);
        adapter.notifyDataSetChanged();

        notifyOutSideDataChanged(adapter.getItems(), adapter.getDataMap());
    }

    private void notifyOutSideDataChanged(List currentItems, Map otherData) {
        if (onDataChangedListener != null) {
            onDataChangedListener.dataChanged(currentItems, otherData);
        }
    }

    public final void reloadData(boolean showLoading) {
        presenter.loadAll(showLoading);
    }

    public interface OnDataChangedListener {
        void dataChanged(List curItems, Map otherData);

    }
}
