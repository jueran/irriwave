package com.irrigate.core.helper.simplifier;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.irrigate.core.net.HttpResult;


/**
 * Description:
 * 只响应有数据返回的成功回调的 {@link BaseTaskListener}
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-17.
 */

public abstract class HaveDataTaskListener<R extends HttpResult> extends BaseTaskListener<R> {
    public void onTaskStart() {
        // do nothing
    }

    public void onTaskCancel() {
        // do nothing
    }

    public void onTaskComplete(@Nullable R result, @Nullable Exception e) {
        // do nothing
    }

    public void onReturnToastMsgErrorCode(@NonNull String errorMsg) {
        // do nothing
    }

    public void onNetworkBroken() {
        // do nothing
    }
}