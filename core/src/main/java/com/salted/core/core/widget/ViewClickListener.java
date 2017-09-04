package com.salted.core.core.widget;

import android.view.View;

import com.salted.core.core.util.ClickUtil;

/**
 * Description:
 * 安全的 listener，适合跳转页面时使用
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-03-26.
 */

public abstract class ViewClickListener implements View.OnClickListener {
    private long doubleClickInterval = 0;

    public ViewClickListener() {
    }

    public ViewClickListener(long doubleClickInterval) {
        this.doubleClickInterval = doubleClickInterval;
    }

    @Override
    public void onClick(View v) {
        if (doubleClickInterval == 0) {
            if (ClickUtil.isFastDoubleClick()) return;
        } else {
            if (ClickUtil.isFastDoubleClick(doubleClickInterval)) return;
        }

        onViewClick(v);
    }

    protected abstract void onViewClick(View v);
}