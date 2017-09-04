package com.irrigate.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.irrigate.R;
import com.irrigate.widget.LoadingViewManager;
import com.salted.core.core.fragment.VisibleStateFragment;
import com.salted.core.core.helper.activityhelper.IViewHelperHolder;
import com.salted.core.core.helper.activityhelper.ViewHelper;
import com.salted.core.core.util.UIUtils;
import com.salted.core.core.widget.BaseToolBar;
import com.salted.core.core.widget.DataStateMaskView;
import com.salted.core.core.widget.ViewClickListener;

/**
 * Description:
 * 带有兼容多样式的自定义错误状态页（包括网络错误和空数据）
 * 建议通过 BaseFragmentFactory {@link BaseFragmentFactory} 的静态工厂方法获得fragment实例，
 * *    并从takeOutYourParam方法中获取数据 (可以不使用)
 * 带有Activity域
 * 继承自LazyFragment，有lazyLoad回调可以进行懒加载
 * 带有VHelper域，可以使用VHelper的特性 {@link ViewHelper}
 * <p>
 * Attention:
 * 副作用是有两个抽象方法需要实现，getContentView()用于返回布局文件，
 * *    onCreateViewCallback就是onCreateView的回调
 * 如果使用ButterKnife，需要手动在 onCreateViewCallback() 中调用 ButterKnife.bind(this,rootView)
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-26.
 */

public abstract class BaseFragment extends VisibleStateFragment implements IViewHelperHolder {
    public static final int FRAGMENT_PADDING_TOP = 66;

    private BaseToolBar toolBar;
    private DataStateMaskView dataStateMaskView;
    private ImageView networkBrokenImage;
    private TextView networkBrokenTv;
    private ImageView emptyDataImage;
    private TextView emptyDataTv;
    private FrameLayout topLayerContainer;
    private SwipeRefreshLayout scrollableContentContainer;
    private FrameLayout swipeLoadingContainer;
    private SwipeRefreshLayout swipeLoadingView;

    protected Activity activity;
    protected ViewHelper vHelper;

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_base_activity_layout, container, false);

        initContentView(inflater, view);
        initViewHelper();
        getDataFromBundle();

        vHelper.onFragmentCreateView(inflater, container, savedInstanceState);
        onCreateViewCallback(view, savedInstanceState);

        return view;
    }

    private void initViewHelper() {
        vHelper = new ViewHelper(activity,
                new LoadingViewManager(activity), toolBar, dataStateMaskView);
        onViewHelperCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        vHelper.onFragmentResumed();
    }

    @Override
    public void onDestroyView() {
        vHelper.onFragmentDestroyView();
        super.onDestroyView();
    }

    /*--------------------------------------------------------------------------------------------*/

    private void initContentView(LayoutInflater inflater, View view) {
        initToolBar(view);
        initErrorDataContainer(view);
        initCustomContentView(getContentView(), view, inflater);
        initTopLayer(view);

        initSwipeLoadingView(view);
    }

    /*-------------------------------------------------*/

    private void initToolBar(View view) {
        toolBar = (BaseToolBar) view.findViewById(R.id.tool_bar);
        if (!showStandardToolBar()) return;
        toolBar.setVisibility(View.VISIBLE);
        toolBar.setBaseInfo(getTitleText(), null);
    }

    /**
     * [override]
     */
    protected boolean showStandardToolBar() {
        return false;
    }

    /**
     * [override]
     * <p>
     * 注意：
     * - 从上一个页面intent取值在这个回调之后执行，所以如果需要取上一个页面传来的值作为标题的话，
     * 需要调用{@link ViewHelper#updateTitle(CharSequence)}
     */
    protected CharSequence getTitleText() {
        return "";
    }

    /*-------------------------------------------------*/

    private void initTopLayer(View view) {
        topLayerContainer = (FrameLayout) view.findViewById(R.id.top_layer_container);
        setTopLayer(getTopLayerLayoutResId());
        setTopLayer(getTopLayerLayout());
    }

    /**
     * TopLayerContainer默认隐藏，通过{@link #setTopLayerContainerVisible(int)}控制其可见性
     * TopLayerContainer位于最上层，在content layer 和 error layer 之上
     *
     * @return top layer layout res id
     */
    protected int getTopLayerLayoutResId() {
        return -1;
    }

    protected View getTopLayerLayout() {
        return null;
    }

    private void setTopLayer(int layoutResID) {
        if (layoutResID <= 0) return;

        topLayerContainer.removeAllViews();
        View.inflate(activity, layoutResID, topLayerContainer);
    }

    private void setTopLayer(View topLayerView) {
        if (topLayerView == null) return;

        topLayerContainer.removeAllViews();
        topLayerContainer.addView(topLayerView);
    }

    protected final void setTopLayerContainerVisible(int visibility) {
        topLayerContainer.setVisibility(visibility);
    }

    /*-------------------------------------------------*/

    private void initErrorDataContainer(View view) {
        dataStateMaskView = (DataStateMaskView) view.findViewById(R.id.data_state_mask_view);
        dataStateMaskView.setEmptyDataView(getEmptyDataView());
        dataStateMaskView.setNetworkBrokenView(getNetworkBrokenView());
    }

    /**
     * [override]
     */
    protected View getEmptyDataView() {
        View layout = View.inflate(activity, R.layout.view_data_empty, null);
        emptyDataImage = (ImageView) layout.findViewById(R.id.no_data_image);
        emptyDataTv = (TextView) layout.findViewById(R.id.no_data_text);
        setViewPaddingTop(emptyDataImage, FRAGMENT_PADDING_TOP);
        return layout;
    }

    /**
     * [override]
     */
    protected View getNetworkBrokenView() {
        View layout = View.inflate(activity, R.layout.view_network_error, null);
        networkBrokenImage = (ImageView) layout.findViewById(R.id.network_broken_image);
        networkBrokenTv = (TextView) layout.findViewById(R.id.network_broken_text);

        layout.findViewById(R.id.reload_btn).setOnClickListener(new ViewClickListener() {
            @Override
            protected void onViewClick(View v) {
                reloadData();
            }
        });

        setViewPaddingTop(networkBrokenImage, FRAGMENT_PADDING_TOP);

        return layout;
    }

    /**
     * [override]
     */
    protected void reloadData() {
        // write your reload data logic
    }

    protected void setNetworkBrokenImage(int imageResId) {
        if (imageResId <= 0) return;
        networkBrokenImage.setImageResource(imageResId);
    }

    protected void setNetworkBrokenText(CharSequence text) {
        if (text == null) return;
        networkBrokenTv.setText(text);
    }

    protected void setEmptyDataImage(int imageResId) {
        if (imageResId <= 0) return;
        emptyDataImage.setImageResource(imageResId);
    }

    protected void setEmptyDataText(CharSequence text) {
        if (text == null) return;
        emptyDataTv.setText(text);
    }

    private void setViewPaddingTop(View view, int paddingInDp) {
        view.setPadding(0, UIUtils.dp2px(activity, paddingInDp), 0, 0);
    }

    /*-------------------------------------------------*/

    private void initSwipeLoadingView(View view) {
        swipeLoadingContainer = (FrameLayout) view.findViewById(R.id.swipe_loading_view_container);
        swipeLoadingView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_loading_view);
        swipeLoadingView.setColorSchemeResources(R.color.c1_1);
        swipeLoadingView.setSize(SwipeRefreshLayout.LARGE);
        swipeLoadingView.setProgressViewEndTarget(false,
                activity.getResources().getDimensionPixelSize(R.dimen.swipe_refresh_layout_end));
    }

    protected void enableSwipeGesture(SwipeRefreshLayout.OnRefreshListener listener) {
        if (swipeLoadingContainer == null) return;
        if (swipeLoadingView == null) return;
        vHelper.setVisible(swipeLoadingContainer, true);
        swipeLoadingView.setOnRefreshListener(listener);
    }

    protected void unableSwipeGesture() {
        if (swipeLoadingContainer == null) return;
        if (swipeLoadingView == null) return;

        vHelper.setVisible(swipeLoadingContainer, false);
    }

    protected void showSwipeLoadingView() {
        if (swipeLoadingContainer == null) return;
        if (swipeLoadingView == null) return;

        vHelper.setVisible(swipeLoadingContainer, true);
        swipeLoadingView.post(new Runnable() {
            @Override
            public void run() {
                swipeLoadingView.setRefreshing(true);
            }
        });
    }

    protected void hideSwipeLoadingView() {
        if (swipeLoadingContainer == null) return;
        if (swipeLoadingView == null) return;

        swipeLoadingView.post(new Runnable() {
            @Override
            public void run() {
                swipeLoadingView.setRefreshing(false);
                vHelper.setVisible(swipeLoadingContainer, false);
            }
        });
    }

    /*-------------------------------------------------*/

    protected boolean isAddContentToSwipeRefreshLayout() {
        return false;
    }

    protected final void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (scrollableContentContainer == null) return;
        scrollableContentContainer.setOnRefreshListener(listener);
    }

//    protected final void showSwipeLoadingView() {
//        if (scrollableContentContainer == null) return;
//        scrollableContentContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollableContentContainer.setRefreshing(true);
//            }
//        });
//    }
//
//    protected final void hideSwipeLoadingView() {
//        if (scrollableContentContainer == null) return;
//        scrollableContentContainer.post(new Runnable() {
//            @Override
//            public void run() {
//                scrollableContentContainer.setRefreshing(false);
//            }
//        });
//    }

    /*-------------------------------------------------*/

    private void initCustomContentView(int layoutResID, View view, LayoutInflater inflater) {
        if (layoutResID <= 0) return;

        if (isAddContentToSwipeRefreshLayout()) {
            scrollableContentContainer =
                    (SwipeRefreshLayout) view.findViewById(R.id.scrollable_content_container);
            scrollableContentContainer.setColorSchemeResources(R.color.c1_1);
            scrollableContentContainer.setVisibility(View.VISIBLE);
            scrollableContentContainer.removeAllViews();
            inflater.inflate(layoutResID, scrollableContentContainer, true);
        } else {
            FrameLayout contentContainer =
                    (FrameLayout) view.findViewById(R.id.frame_content_container);
            contentContainer.setVisibility(View.VISIBLE);
            contentContainer.removeAllViews();
            inflater.inflate(layoutResID, contentContainer, true);
        }
    }

    protected abstract int getContentView();

    protected abstract void onCreateViewCallback(View rootView, Bundle savedInstanceState);

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 使用MVP的时候，适合在这初始化P，这样就能在{@link #takeOutYourParam(Parcelable)}中使用P把数据传给同时
     * 初始化了的M
     */
    protected void onViewHelperCreate() {
        // 初始化P
    }

    private void getDataFromBundle() {
        BaseFragmentFactory.analysisData(getArguments(),
                new BaseFragmentFactory.TakeOutParamCallBack() {
                    @Override
                    public void takeOutParam(String stringParam) {
                        takeOutYourParam(stringParam);
                    }

                    @Override
                    public void takeOutParam(Parcelable param) {
                        try {
                            takeOutYourParam(param);
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 1. Param is not null.
     * 2. If you newInstance with param, you can cast it to your custom param and use it.
     */
    protected void takeOutYourParam(@NonNull Parcelable param) throws ClassCastException {
        // use  param

        // example
        //        if (param instanceof YourClass) {
        //            presenter.initPayInfo((YourClass) param);
        //        }
    }

    protected void takeOutYourParam(@NonNull String param) {
        // use  param
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public ViewHelper getViewHelper() {
        return vHelper;
    }
}