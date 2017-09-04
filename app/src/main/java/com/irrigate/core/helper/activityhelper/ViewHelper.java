package com.irrigate.core.helper.activityhelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ScrollView;

import com.irrigate.R;
import com.irrigate.core.helper.baseinterface.ILoadingView;
import com.irrigate.core.helper.callback.ActivityLifecycleCallbacks;
import com.irrigate.core.helper.callback.FragmentLifecycleCallbacks;
import com.irrigate.core.helper.systemadvance.KeyboardStatusWatcher;
import com.irrigate.core.helper.systemadvance.StatusBarManager;
import com.irrigate.core.util.ToastUtil;
import com.irrigate.core.util.UIUtils;
import com.irrigate.core.widget.BaseToolBar;
import com.irrigate.core.widget.DataStateMaskView;

import org.greenrobot.eventbus.EventBus;

/**
 * Description:
 * 有很多实用的特性：
 * - 带有设置StatusBar颜色方法
 * - 带有显示/隐藏view方法
 * - 带有向下滑出finish方法
 * - 带有弹出网络错误浮层方法
 * - 带有收起虚拟键盘方法
 * - 及其他实用代理方法
 * <p>
 * Attention:
 * - 建议在基类Activity和Fragment的生命周期方法中调用ViewHelper的不为空的生命周期回调方法
 * {@link ActivityLifecycleCallbacks} {@link FragmentLifecycleCallbacks}
 * - 如果有ToolBar和ErrorStateView的话，在初始化完toolBar和errorStateView之后初始化ViewHelper，所以在
 * Activity中一般ViewHelper会在onCreated()之后被初始化，而Fragment中在onCreateView()之后被初始化
 * - 如果不使用ToolBar和ErrorStateView，也可以通过一个参数的构造方法初始化，不过只能使用ViewHelper中的一部分方法
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-15.
 */

@SuppressWarnings(value = {"PMD.GodClass", "PMD.ExcessivePublicCount",
        "PMD.TooManyMethods", "PMD.UncommentedEmptyMethodBody"})
public class ViewHelper implements ActivityLifecycleCallbacks, FragmentLifecycleCallbacks {
    private Activity activity;
    private Handler handler;
    private BaseToolBar toolBar;
    private DataStateMaskView errorView;
    private ILoadingView loadingView;

    public ViewHelper(@NonNull Activity activity, @NonNull ILoadingView loadingView) {
        this.activity = activity;
        this.loadingView = loadingView;
        handler = new Handler();
    }

    public ViewHelper(@NonNull Activity activity, @NonNull ILoadingView loadingView,
                      @NonNull BaseToolBar toolBar) {
        this.activity = activity;
        this.toolBar = toolBar;
        this.loadingView = loadingView;
        handler = new Handler();
    }

    public ViewHelper(@NonNull Activity activity, @NonNull ILoadingView loadingView,
                      @NonNull DataStateMaskView errorView) {
        this.activity = activity;
        this.errorView = errorView;
        this.loadingView = loadingView;
        handler = new Handler();
    }

    public ViewHelper(@NonNull Activity activity, @NonNull ILoadingView loadingView,
                      @NonNull BaseToolBar toolBar, @NonNull DataStateMaskView errorView) {
        this.activity = activity;
        this.toolBar = toolBar;
        this.errorView = errorView;
        this.loadingView = loadingView;
        handler = new Handler();
    }

    /*--------------------------------------------------------------------------------------------*/

    public void show(View view) {
        UIUtils.show(view);
    }

    public void hide(View view) {
        UIUtils.hide(view);
    }

    public void invisible(View view) {
        UIUtils.invisible(view);
    }

    public void setVisible(View view, boolean visible) {
        UIUtils.setVisible(view, visible);
    }

    public void setVisible(boolean visible, View... views) {
        if (views == null) return;
        for (View view : views) {
            UIUtils.setVisible(view, visible);
        }
    }

    public void setVisible(View view, boolean visible, boolean useInvisibleInsteadGone) {
        UIUtils.setVisible(view, visible, useInvisibleInsteadGone);
    }

    /*-------------------------------------------------*/

    public void toastNetworkBroken() {
        toast(activity.getResources().getString(R.string.network_error_tips));
    }

    public void toast(String string) {
        ToastUtil.toastShort(activity, string);
    }

    public void toast(String mainText, String defaultText) {
        toast(TextUtils.isEmpty(mainText) ? defaultText : mainText);
    }

    /*-------------------------------------------------*/

    private KeyboardStatusWatcher keyboardStatusWatcher;

    public void hideKeyboard() {
        UIUtils.dismissInputmethod(activity);
//        UIUtils.hideKeyBoard(activity,handler);
    }

    public void showKeyboard(EditText edit) {
        UIUtils.showInputmethod(edit);
    }

    public void moveCursorToEnd(EditText edit) {
        UIUtils.moveCursorToEnd(edit);
    }

    public void addKeyboardStatusWatcher(KeyboardStatusWatcher.VisibilityListener listener) {
        keyboardStatusWatcher = new KeyboardStatusWatcher(activity, listener);
        keyboardStatusWatcher.attachKeyboardWatcher();
    }

    public void removeKeyboardStatusWatcher() {
        if (keyboardStatusWatcher == null) return;
        keyboardStatusWatcher.removeKeyboardWatcher();
    }

    /*-------------------------------------------------*/

    public void setStatusBarLightMode(int lightColorResId) {
        StatusBarManager.setStatusBarLightMode(activity, lightColorResId);
    }

    public void setStatusBarBackgroundColorId(int colorResId) {
        StatusBarManager.setStatusBarBackgroundColorId(colorResId, activity);
    }

    public void setStatusBarBackgroundColor(int color) {
        StatusBarManager.setStatusBarBackgroundColor(color, activity);
    }

    public StatusBarManager.StatusBarSetResult setStatusBarDarkWords() {
        return StatusBarManager.setStatusBarDarkWords(activity);
    }

    public int getStatusBarHeight() {
        return StatusBarManager.getStatusBarHeight(activity);
    }

    /*-------------------------------------------------*/

    /**
     * 在 onActivityDestroyed 时会清空回调，保证安全
     */
    public void post(Runnable r) {
        handler.post(r);
    }

    /**
     * 在 onActivityDestroyed 时会清空回调，保证安全
     */
    public void postDelayed(Runnable r, long delayMillis) {
        handler.postDelayed(r, delayMillis);
    }

    /*-------------------------------------------------*/

    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    /*-------------------------------------------------*/

    public void scrollToTop(AbsListView absListView) {
        absListView.scrollBy(0, -absListView.getScrollY());
    }

    public void scrollToTop(ScrollView scrollView) {
        scrollView.scrollBy(0, -scrollView.getScrollY());
    }

    public void scrollToTop(RecyclerView recyclerView) {
        recyclerView.scrollToPosition(0);
    }

    public void scrollToTop(WebView webView) {
        webView.scrollBy(0, -webView.getScrollY());
    }

    public void scrollToTop(NestedScrollView scrollView) {
        scrollView.scrollBy(0, -scrollView.getScrollY());
    }

    public void smoothScrollToTop(AbsListView absListView) {
        absListView.smoothScrollToPosition(0);
    }

    public void smoothScrollToTop(ScrollView scrollView) {
        scrollView.smoothScrollBy(0, -scrollView.getScrollY());
    }

    /**
     * This smooth scroll effect depends on different LayoutManager, recommend you run app and
     * *    check it out.
     * <p>
     * LinearLayoutManager have bad smooth scroll effect when have too many items.
     */
    @Deprecated
    public void smoothScrollToTop(RecyclerView recyclerView) {
        recyclerView.smoothScrollToPosition(0);
    }

    /**
     * Wait for you implement!
     */
    @Deprecated
    public void smoothScrollToTop(WebView webView) {
    }

    public void smoothScrollToTop(NestedScrollView scrollView) {
        scrollView.smoothScrollBy(0, -scrollView.getScrollY());
    }

    /*-------------------------------------------------*/

    public Activity getActivity() {
        return activity;
    }

    /*--------------------------------------------------------------------------------------------*/

    public BaseToolBar getToolBar() {
        return toolBar;
    }

    public void configToolBar(CharSequence title, View.OnClickListener backClickListener) {
        if (toolBar == null) return;
        toolBar.setBaseInfo(title, backClickListener);
    }

    public void updateTitle(CharSequence title) {
        if (toolBar == null) return;
        toolBar.updateTitle(title);
    }

    /**
     * 设置返回按钮的 icon 为“X”，同时页面返回时自带向下滑动退出动画
     */
    public void setBackBtCloseIcon() {
        if (toolBar == null) return;
        toolBar.setBackBtCloseIcon();
        isBackBtCloseIcon = true;
    }

    private boolean isBackBtCloseIcon = false;

    /**
     * 在ToolBar右侧添加按钮
     * <p>
     * 添加顺序从右向左
     * 默认隐藏，建议在网络请求成功后调用 {@link ViewHelper#showToolBarBtns()} 来显示按钮
     * 可以同时添加文字按钮和图片按钮
     * 由于位置限制，最多添加2个按钮，文字按钮名字建议最长4个字
     */
    public void addToolBarBtn(String btnText, View.OnClickListener clickListener) {
        if (toolBar == null) return;
        toolBar.addButton(btnText, clickListener);
    }

    /**
     * 在ToolBar右侧添加按钮
     * <p>
     * 添加顺序从右向左
     * 默认隐藏，建议在网络请求成功后调用 {@link ViewHelper#showToolBarBtns()} 来显示按钮
     * 可以同时添加文字按钮和图片按钮
     * 由于位置限制，最多添加2个按钮
     */
    public void addToolBarBtn(int iconResId, View.OnClickListener clickListener) {
        if (toolBar == null) return;
        toolBar.addButton(iconResId, clickListener);
    }

    public void showToolBarBtns() {
        if (toolBar == null) return;
        toolBar.showToolBarBtns();
    }

    public void hideToolBarBtns() {
        if (toolBar == null) return;
        toolBar.hideToolBarBtns();
    }

    public void setToolBarBtnsVisible(boolean visible) {
        if (toolBar == null) return;
        if (visible) {
            toolBar.showToolBarBtns();
        } else {
            toolBar.hideToolBarBtns();
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    public DataStateMaskView getErrorView() {
        return errorView;
    }

    public void showNetworkBrokenView() {
        if (errorView == null) return;
        errorView.showNetworkBroken();
    }

    public void showEmptyDataView() {
        if (errorView == null) return;
        errorView.showEmptyData();
    }

    public void showHaveDataView() {
        if (errorView == null) return;
        errorView.hideAll();
    }

    // 第一次Loading的时候，整个页面需要用空白的view遮罩，如果网络错误就直接从空白变成网络错误
    public void showMaskView() {
        if (errorView == null) return;
        errorView.showLoadingState();
    }

    /*--------------------------------------------------------------------------------------------*/

    // 可以多次调用，一个ViewHelper只会显示一个loadingView
    public void showLoadingView() {
        hideKeyboard();
        loadingView.showLoadingView();
    }

    public void hideLoadingView() {
        loadingView.hideLoadingView();
    }

    public void hideLoadingViewWithoutAnim() {
        loadingView.hideLoadingViewWithoutAnim();
    }

    public void hideLoadingViewDelayed(long time) {
        loadingView.hideLoadingViewDelayed(time);
    }

    public boolean isLoadingViewShowing() {
        return loadingView.isShowing();
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        PushUtils.initPush(activity);
    }

    @Override
    public void onActivityStarted() {

    }

    @Override
    public void onActivityResumed() {
        if (!loadingView.isShowing()) {
            loadingView.hideLoadingViewWithoutAnim();
        }
    }

    @Override
    public void onActivityPaused() {

    }

    @Override
    public void onActivityStopped() {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState) {

    }

    @Override
    public void onActivityFinish() {
        if (isBackBtCloseIcon) activity.overridePendingTransition(0, R.anim.push_out_down);
    }

    @Override
    public void onActivityDestroyed() {
        hideKeyboard();
        removeKeyboardStatusWatcher();
        if (handler != null) handler.removeCallbacksAndMessages(null); // 清空所有callback
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onFragmentAttach(Context context) {
        // 不建议在这做事情，因为这时候ViewHelper还没有初始化
    }

    @Override
    public void onFragmentCreate(Bundle savedInstanceState) {
        // 不建议在这做事情，因为这时候ViewHelper还没有初始化
    }

    @Override
    public void onFragmentCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentStarted() {

    }

    @Override
    public void onFragmentResumed() {
        if (!loadingView.isShowing()) {
            loadingView.hideLoadingViewWithoutAnim();
        }
    }

    @Override
    public void onFragmentPaused() {

    }

    @Override
    public void onFragmentStopped() {

    }

    @Override
    public void onFragmentDestroyView() {
        hideKeyboard();
        removeKeyboardStatusWatcher();
        if (handler != null) handler.removeCallbacksAndMessages(null); // 清空所有callback
    }

    @Override
    public void onFragmentDestroy() {

    }

    @Override
    public void onFragmentDetach() {

    }
}