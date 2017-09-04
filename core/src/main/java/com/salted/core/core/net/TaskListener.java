package com.salted.core.core.net;


public interface TaskListener<T> {

    void onTaskStart(TaskListener<T> listener);

    void onTaskComplete(TaskListener<T> listener, T result, Exception e);

    void onCancel();
}
