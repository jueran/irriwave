package com.irrigate.base.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.irrigate.BuildConfig;
import com.irrigate.R;
import com.irrigate.core.helper.activityhelper.ActivityRouter;
import com.irrigate.core.helper.activityhelper.IViewHelperHolder;
import com.irrigate.core.helper.activityhelper.ViewHelper;
import com.irrigate.core.net.HQHttpRequest;
import com.irrigate.core.util.UIUtils;
import com.irrigate.core.widget.BaseToolBar;
import com.irrigate.core.widget.DataStateMaskView;
import com.irrigate.core.widget.ViewClickListener;
import com.irrigate.widget.LoadingViewManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Description:
 * BaseActivity具有很多实用的特性：
 * - 带有兼容多样式的自定义ToolBar
 * - 在onBack()中统一处理ToolBar按钮返回和物理键返回事件
 * - 带有兼容多样式的自定义错误状态页（包括网络错误和空数据）
 * - 带有多个静态启动方法，并从takeOutYourParam方法中获取数据 (可以不使用)
 * - 带有注册EventBus开关（自动注销）
 * - 带有Activity域
 * - 带有VHelper域，可以使用VHelper的特性 {@link ViewHelper}
 * - 默认注册推送
 * <p>
 * Attention:
 * - 如果使用ButterKnife，需要手动在 onCreated() 中 super 之后调用 ButterKnife.bind(this)
 * - 子类需要实现两个抽象方法，返回toolbar标题和内容区layout
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-14.
 */

@SuppressLint("all")
@SuppressWarnings({"PMD", "checkstyle:all"})
public abstract class BaseActivity extends AppCompatActivity implements IViewHelperHolder {
    public static final int ACTIVITY_ERROR_VIEW_PADDING_TOP = 93;

    private BaseToolBar toolBar;
    private DataStateMaskView dataStateMaskView;
    private ImageView networkBrokenImage;
    private TextView networkBrokenTv;
    private ImageView emptyDataImage;
    private TextView emptyDataTv;
    private FrameLayout topLayerContainer;
    private SwipeRefreshLayout scrollableContentContainer;

    protected ViewHelper vHelper;

    /*--------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        safeCallOnCreate(savedInstanceState);
    }

    private void safeCallOnCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            baseCreate(savedInstanceState);
            onSafeCreate(savedInstanceState);
            return;
        }

        try {
            baseCreate(savedInstanceState);
            onSafeCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void baseCreate(Bundle savedInstanceState) {
        initContentView();
        initViewHelper();
        configActivity();
        getDataFromIntent();

        vHelper.onActivityCreated(savedInstanceState);
    }

    /**
     * try catch 了的 onCreate 回调
     */
    protected void onSafeCreate(Bundle savedInstanceState) {

    }

    private void initViewHelper() {
        vHelper = new ViewHelper(getActivity(), new LoadingViewManager(getActivity()),
                toolBar, dataStateMaskView);
        onViewHelperCreate();
    }

    protected Activity getActivity() {
        return this;
    }

    private void configActivity() {
        registerEventBus();
        vHelper.setStatusBarBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onResume() {
        super.onResume();
        vHelper.onActivityResumed();
    }

    @Override
    public void finish() {
        vHelper.onActivityFinish();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        vHelper.onActivityDestroyed();
        unregisterEventBus();
        cancelAllRequests(); // 保证在使用者直接调用 finish 方法时也能 cancelAllRequests

        super.onDestroy();
    }

    /*--------------------------------------------------------------------------------------------*/

    protected final boolean cancelAllRequests() {
        if (HQHttpRequest.isActivityHasRequesting(this)) {
            HQHttpRequest.cancelActivityAllRequests(this);
            return true;
        } else {
            return false;
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * [override]
     */
    protected boolean isEnableEventBus() {
        return false;
    }

    private void registerEventBus() {
        if (isEnableEventBus()) EventBus.getDefault().register(this);
    }

    private void unregisterEventBus() {
        if (isEnableEventBus()) EventBus.getDefault().unregister(this);
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 使用MVP的时候，适合在这初始化P，这样就能在{@link #takeOutYourParam(Parcelable)}中使用P把数据传给同时
     * 初始化了的M
     */
    protected void onViewHelperCreate() {
        // 初始化P
    }

    private void getDataFromIntent() {
        ActivityRouter.analysisDataThroughRouter(getIntent(),
                new ActivityRouter.TakeOutParamCallBack() {
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
     * [override]
     * <p>
     * Param is not null.
     * If you start activity with param, you can cast it to your custom param and use it.
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

    private void initContentView() {
        setContentView(R.layout.activity_base_activity_layout);

        initToolBar();
        initDataStateMaskView();

        initCustomContentView(getContentView());
        initTopLayer();
    }

    /**
     * Use like getContentView {@link AppCompatActivity#setContentView(int)}
     *
     * @return content layout res id
     */
    protected abstract int getContentView();

    private void initTopLayer() {
        topLayerContainer = (FrameLayout) findViewById(R.id.top_layer_container);
        initTopLayer(getTopLayerLayoutResId());
        initTopLayer(getTopLayerLayout());
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

    /*-------------------------------------------------*/

    private void initToolBar() {
        toolBar = (BaseToolBar) findViewById(R.id.tool_bar);
        if (!showStandardToolBar()) return;
        toolBar.setVisibility(View.VISIBLE);
        toolBar.setBaseInfo(getTitleText(), new ViewClickListener() {
            @Override
            protected void onViewClick(View v) {
                onBack();
            }
        });
    }

    /**
     * [override]
     */
    protected boolean showStandardToolBar() {
        return true;
    }

    /**
     * [override]
     * <p>
     * 注意：
     * - 从上一个页面intent取值在这个回调之后执行，所以如果需要取上一个页面传来的值作为标题的话，
     * 需要调用{@link ViewHelper#updateTitle(CharSequence)}
     */
    protected abstract CharSequence getTitleText();

    /**
     * [call] / [override]
     * <p>
     * 满足此产品文档中需求：
     * 发起网络请求后，在加载过程中点击物理返回键：
     * 1、第一次点击，中止网络请求，隐藏网络加载进度条，停留在当前界面
     * 2、第二次点击，返回上一级/退出App
     * 如果无网络请求，直接返回上一级
     * <p>
     * 不建议重写此方法，建议重写 {@link #onSafeBack()}
     * 建议使用自己toolbar的时候，点击返回按钮时调用此方法，然后有必要的时候重写 {@link #onSafeBack()}
     */
    protected void onBack() {
        vHelper.hideKeyboard();
        if (cancelAllRequests()) return;
        onSafeBack();
    }

    /**
     * [override]
     * <p>
     * 在当前activity没有网络请求时点击物理返回键或返回键时调用
     * 默认会finish当前的页面，如果需要弹窗提示或另外提交数据，重写此方法
     */
    protected void onSafeBack() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*-------------------------------------------------*/

    private void initDataStateMaskView() {
        dataStateMaskView = (DataStateMaskView) findViewById(R.id.data_state_mask_view);
        dataStateMaskView.setEmptyDataView(getEmptyDataView());
        dataStateMaskView.setNetworkBrokenView(getNetworkBrokenView());
    }

    /**
     * [override]
     */
    protected View getEmptyDataView() {
        View layout = View.inflate(getActivity(), R.layout.view_data_empty, null);
        emptyDataImage = (ImageView) layout.findViewById(R.id.no_data_image);
        emptyDataTv = (TextView) layout.findViewById(R.id.no_data_text);
        setViewPaddingTop(emptyDataImage, ACTIVITY_ERROR_VIEW_PADDING_TOP);
        return layout;
    }

    /**
     * [override]
     */
    protected View getNetworkBrokenView() {
        View layout = View.inflate(getActivity(), R.layout.view_network_error, null);
        networkBrokenImage = (ImageView) layout.findViewById(R.id.network_broken_image);
        networkBrokenTv = (TextView) layout.findViewById(R.id.network_broken_text);
        setViewPaddingTop(networkBrokenImage, ACTIVITY_ERROR_VIEW_PADDING_TOP);

        layout.findViewById(R.id.reload_btn).setOnClickListener(new ViewClickListener() {
            @Override
            protected void onViewClick(View v) {
                reloadData();
            }
        });

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
        view.setPadding(0, UIUtils.dp2px(getActivity(), paddingInDp), 0, 0);
    }

    /*-------------------------------------------------*/

    private void initCustomContentView(int layoutResID) {
        if (layoutResID <= 0) return;

        View childView = getLayoutInflater().inflate(layoutResID, null);

        if (isAddContentToSwipeRefreshLayout()) {
            scrollableContentContainer = (SwipeRefreshLayout) findViewById(R.id.scrollable_content_container);
            scrollableContentContainer.setColorSchemeResources(R.color.c1_1);
            scrollableContentContainer.setVisibility(View.VISIBLE);
            scrollableContentContainer.removeAllViews();
            scrollableContentContainer.addView(childView);
        } else {
            FrameLayout contentContainer = (FrameLayout) findViewById(R.id.frame_content_container);
            contentContainer.setVisibility(View.VISIBLE);
            contentContainer.removeAllViews();
            contentContainer.addView(childView);
        }
    }

    /*-------------------------------------------------*/

    private void initTopLayer(int layoutResID) {
        if (layoutResID <= 0) return;

        topLayerContainer.removeAllViews();
        getLayoutInflater().inflate(layoutResID, topLayerContainer, true);
    }

    private void initTopLayer(View topLayerView) {
        if (topLayerView == null) return;

        topLayerContainer.removeAllViews();
        topLayerContainer.addView(topLayerView);
    }

    protected final void setTopLayerContainerVisible(int visibility) {
        topLayerContainer.setVisibility(visibility);
    }

    /*-------------------------------------------------*/

    protected boolean isAddContentToSwipeRefreshLayout() {
        return false;
    }

    protected final void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (scrollableContentContainer == null) return;
        scrollableContentContainer.setOnRefreshListener(listener);
    }

    protected final void showSwipeLoadingView() {
        if (scrollableContentContainer == null) return;
        scrollableContentContainer.post(new Runnable() {
            @Override
            public void run() {
                scrollableContentContainer.setRefreshing(true);
            }
        });
    }

    protected final void hideSwipeLoadingView() {
        if (scrollableContentContainer == null) return;
        scrollableContentContainer.post(new Runnable() {
            @Override
            public void run() {
                scrollableContentContainer.setRefreshing(false);
            }
        });
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public ViewHelper getViewHelper() {
        return vHelper;
    }
}