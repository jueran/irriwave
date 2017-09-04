package com.salted.core.core.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * fragment是否可见
 */
public class VisibleStateFragment extends Fragment {
    private boolean isViewCreated;

    @Override
    public void onResume() {
        super.onResume();
        tryChangeVisibleState(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        tryChangeVisibleState(false);
    }

    /**
     * 不要尝试用这种方式响应fragment的显示状态变化，用下面的方法<br/>
     * 隐藏：{@link #onRealHide() }<br/>
     * 显示：{@link #onRealVisible() }<br/>
     */
    @Deprecated
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        tryChangeVisibleState(isVisibleToUser);
    }

    private void tryChangeVisibleState(boolean visible) {
        boolean getVisible = getUserVisibleHint();
        if (isViewCreated && visible && isVisible() && getVisible) {
            onRealVisible();
        }
        if (isViewCreated && !visible && !getVisible) {
            onRealHide();
        }
    }

    boolean hasRealVisibleFirstTime = false;

    protected void onRealVisibleFirstTime() {
        // Fragment第一次可见
    }

    @CallSuper
    protected void onRealVisible() {
        if (!hasRealVisibleFirstTime) {
            hasRealVisibleFirstTime = true;
            onRealVisibleFirstTime();
        }
        // Fragment可见
    }

    protected void onRealHide() {
        // Fragment不可见
    }

    /*--------------------------------------------------------------------------------------------*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
    }
}

