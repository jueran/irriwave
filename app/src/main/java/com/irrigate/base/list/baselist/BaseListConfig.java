package com.irrigate.base.list.baselist;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by xinyuanzhong on 2017/6/20.
 */

public class BaseListConfig {

    //空页面文案
    private CharSequence emptyTip;

    //空页面图片
    @DrawableRes
    private int emptyPicRes;

    //空页面按钮文案
    private CharSequence emptyBtnText;

    //空页面按钮点击事件
    private View.OnClickListener emptyBtnClickListener;

    //是否使用懒加载
    private boolean lazyMode;

    //是否托管空页面处理（某些情况下，空页面不是铺满整个页面，如课程详情，这时候你需要设为false然后自己处理空页面）
    private boolean autoControlEmptyView = true;

    /**
     * 使用自定义的空页面（当默认的empty_view样式不能满足需求，需要重新写一个layout传进来）
     * 注意和autoControlEmptyView的区别，autoControlEmptyView是放弃自带的空页面显示逻辑，
     * 而customEmptyLayout是继续使用默认逻辑，只是换R.layout.empty_view为其他布局
     */
    @LayoutRes
    private int customEmptyLayoutRes;

    //是否使用弹窗加载（默认用SwipeRefreshLayout样式）
    private boolean useDialogLoading;

    //tab主题色
    @ColorRes
    private int tabColorRes;

    //是否禁用下拉刷新
    private boolean disableSwipeRefresh;

    //是否禁用右下角回到顶部
    private boolean disableScrollToTop = true;

    //第一次进入页面是否显示列表
    //有些带Header的列表如果需要首次进入展示头部默认布局，设为true
    private boolean showListWhenFirstIn;

    private ItemDivider itemDivider;

    public boolean isDisableScrollToTop() {
        return disableScrollToTop;
    }

    public void setDisableScrollToTop(boolean disableScrollToTop) {
        this.disableScrollToTop = disableScrollToTop;
    }

    public boolean isDisableSwipeRefresh() {
        return disableSwipeRefresh;
    }

    public void setDisableSwipeRefresh(boolean disableSwipeRefresh) {
        this.disableSwipeRefresh = disableSwipeRefresh;
    }

    public boolean isUseDialogLoading() {
        return useDialogLoading;
    }

    public void setUseDialogLoading(boolean useDialogLoading) {
        this.useDialogLoading = useDialogLoading;
    }

    public CharSequence getEmptyTip() {
        return emptyTip;
    }

    public void setEmptyTip(CharSequence emptyTip) {
        this.emptyTip = emptyTip;
    }

    public int getEmptyPicRes() {
        return emptyPicRes;
    }

    public void setEmptyPicRes(@DrawableRes int emptyPicRes) {
        this.emptyPicRes = emptyPicRes;
    }

    public CharSequence getEmptyBtnText() {
        return emptyBtnText;
    }

    public void setEmptyBtnText(CharSequence emptyBtnText) {
        this.emptyBtnText = emptyBtnText;
    }

    public View.OnClickListener getEmptyBtnClickListener() {
        return emptyBtnClickListener;
    }

    public void setEmptyBtnClickListener(View.OnClickListener emptyBtnClickListener) {
        this.emptyBtnClickListener = emptyBtnClickListener;
    }

    public boolean isLazyMode() {
        return lazyMode;
    }

    public void setLazyMode(boolean lazyMode) {
        this.lazyMode = lazyMode;
    }

    public int getCustomEmptyLayoutRes() {
        return customEmptyLayoutRes;
    }

    public void setCustomEmptyLayoutRes(@LayoutRes int customEmptyLayoutRes) {
        this.customEmptyLayoutRes = customEmptyLayoutRes;
    }

    public boolean isAutoControlEmptyView() {
        return autoControlEmptyView;
    }

    @ColorRes
    public int getTabColorRes() {
        return tabColorRes;
    }

    public void setTabColorRes(@ColorRes int tabColorRes) {
        this.tabColorRes = tabColorRes;
    }


    public boolean isShowListWhenFirstIn() {
        return showListWhenFirstIn;
    }

    public void setShowListWhenFirstIn(boolean showListWhenFirstIn) {
        this.showListWhenFirstIn = showListWhenFirstIn;
    }

    public ItemDivider getItemDivider() {
        return itemDivider;
    }

    public void setItemDivider(ItemDivider itemDecoration) {
        this.itemDivider = itemDecoration;
    }
}
