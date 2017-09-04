package com.irrigate.core.helper.callback;

import android.os.Bundle;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-05-15.
 */

public interface ActivityLifecycleCallbacks {
    void onActivityCreated(Bundle savedInstanceState);

    void onActivityStarted();

    void onActivityResumed();

    void onActivityPaused();

    void onActivityStopped();

    void onActivitySaveInstanceState(Bundle outState);

    void onActivityFinish();

    void onActivityDestroyed();
}