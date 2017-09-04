package com.irrigate.core.util;


import com.irrigate.BuildConfig;

import junit.framework.Assert;

/**
 * 说明：
 * 作者：杨健
 * 时间：2017/8/15.
 */

public final class ExceptionUtil {
    private ExceptionUtil() {

    }

    /**
     * 正常运行时,乎略exception
     * 但调试时,不乎略
     *
     * @param e
     */
    public static void ignore(Exception e) {
        e.printStackTrace();
        if (BuildConfig.DEBUG) {
            Assert.assertNull(e);
        }
    }
}
