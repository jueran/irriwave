package com.irrigate.core.net;

/**
 * 说明：
 * 作者：杨健
 * 时间：2017/5/16.
 */

public interface HQAsyncTask<T> {
    void execute();
    void cancel();
    void setTaskListener(TaskListener<T> listener);
}
