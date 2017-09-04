package com.irrigate.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-30.
 */

public class LazyFragment extends Fragment {
    private boolean isViewCreated;
    private boolean isVisible;
    private boolean hasLoadOnce;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            tryLazyLoad();
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;

        tryLazyLoad();
    }

    private void tryLazyLoad() {
        if (!isVisible || !isViewCreated) return;

        onSafeVisible();

        if (!hasLoadOnce) {
            lazyLoad();
            hasLoadOnce = true;
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 此方法会只会在第一次被显示同时ViewCreated结束后被调用
     */
    protected void lazyLoad() {
        // 此方法会只会在第一次被显示同时ViewCreated结束后被调用
    }

    /**
     * 此方法会在每一次被显示同时ViewCreated结束后被调用
     * <p>
     * 注意：
     * - 这里的“被显示”只限制于表示在被良好管理的FragmentManager中切换fragment后的被显示，比如viewpager中，这
     * .   这是由于{@link #setUserVisibleHint(boolean)}只会在这种情况被回调
     * . - 切换activity导致的fragment被显示不会回调此方法
     * . - 管理有问题的FragmentManager中切换fragment后的被显示也不会回调此方法
     */
    protected void onSafeVisible() {
        // 此方法会在每一次被显示同时ViewCreated结束后被调用
    }

    /*--------------------------------------------------------------------------------------------*/

    /**
     * 此方法会在每一次被显示时被调用
     * 不建议使用，同{@link #onSafeVisible()}
     */
    @Deprecated
    protected void onVisible() {
        // 不建议使用
    }

    @Deprecated
    protected void onInvisible() {
        // 不建议使用
    }
}

