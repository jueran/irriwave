package com.irrigate.core.util;

import com.irrigate.BuildConfig;

/**
 * Description:
 * <p>
 * Attention:
 * <p>
 * Created by Zhengyu.Xiong ; On 2017-08-16.
 */

public final class SandBox {
    private SandBox() {
    }

    public static void start(Runnable runnable) {
        if (BuildConfig.DEBUG) {
            runnable.run();
        } else {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}