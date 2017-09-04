package com.irrigate.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.irrigate.R;
import com.irrigate.core.util.UIUtils;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-26.
 */

public class DataStateMaskView extends FrameLayout {
    private FrameLayout emptyDataViewContainer;
    private FrameLayout networkBrokenViewContainer;

    private STATUS status = STATUS.HAVE_DATA;

    public DataStateMaskView(Context context) {
        super(context);
        initView(context);
    }

    public DataStateMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DataStateMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.setBackgroundColor(getResources().getColor(R.color.global_background_color));
        this.setClickable(true); // 防止点击穿透

        LayoutInflater.from(context).inflate(R.layout.view_data_state_mask, this, true);
        emptyDataViewContainer = (FrameLayout)
                findViewById(R.id.data_state_mask_view_empty_data_view_container);
        networkBrokenViewContainer = (FrameLayout)
                findViewById(R.id.data_state_mask_view_network_broken_view_container);

        this.setVisibility(GONE);
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 完全自定义空数据页面
     */
    public void setEmptyDataView(View customView) {
        emptyDataViewContainer.removeAllViews();
        emptyDataViewContainer.addView(customView);
    }

    /**
     * 完全自定义网络错误页面
     */
    public void setNetworkBrokenView(View customView) {
        networkBrokenViewContainer.removeAllViews();
        networkBrokenViewContainer.addView(customView);
    }

    /*-------------------------------------------------*/

    public void showNetworkBroken() {
        status = STATUS.NETWORK_BROKEN;

        this.setVisibility(VISIBLE);

        setNetworkBrokenViewVisible(true);
        setEmptyDataViewVisible(false);
    }

    public void showEmptyData() {
        status = STATUS.EMPTY_DATA;

        this.setVisibility(VISIBLE);

        setEmptyDataViewVisible(true);
        setNetworkBrokenViewVisible(false);
    }

    // 完全空白，建议第一次loading的时候设置为这个状态
    public void showLoadingState() {
        status = STATUS.LOADING_STATE;

        this.setVisibility(VISIBLE);

        setEmptyDataViewVisible(false);
        setNetworkBrokenViewVisible(false);
    }

    public void hideAll() {
        status = STATUS.HAVE_DATA;

        this.setVisibility(GONE);
    }

    public STATUS getStatus() {
        return status;
    }

    /*--------------------------------------------------------------------------------------------*/

    private void setNetworkBrokenViewVisible(boolean visible) {
        UIUtils.setVisible(networkBrokenViewContainer, visible);
    }

    private void setEmptyDataViewVisible(boolean visible) {
        UIUtils.setVisible(emptyDataViewContainer, visible);
    }

    /*--------------------------------------------------------------------------------------------*/

    public enum STATUS {
        NETWORK_BROKEN,
        EMPTY_DATA,
        HAVE_DATA,
        LOADING_STATE
    }
}