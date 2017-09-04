package com.irrigate.core.helper.baseinterface;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-08-02.
 */

public interface ILoadingView {
    void showLoadingView();

    void hideLoadingView();

    void hideLoadingViewWithoutAnim();

    void hideLoadingViewDelayed(final long delayMillis);

    /*-------------------------------------------------*/

    boolean isShowing();
}