package com.salted.core.core.net;

import android.content.Context;

import com.salted.core.BuildConfig;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>说明：串行任务组工具类</h1>
 * <h2>让一组网络请求串行执行</h2>
 * <ul>
 * <li>对于每个task成员，在task结束后会立即回调自己的onComplete。
 * {@link #onAnyTaskComplete(HttpResult, Exception)}</li>
 * <li>对于整个task组，在task组结束后会立即回调task组的onComplete方法；
 * task组结束的条件组是每个task都成功结束或某一个task成员失败结束。</li>
 * <li>对于每个task成员，在task结束后会立即回调自己的onComplete。
 * {@link #onAnyTaskComplete(HttpResult, Exception)}</li>
 * <li>对于整个task组，在task组结束后会立即回调task组的onComplete方法；
 * task组结束的条件组是每个task都成功结束或某一个task成员失败结束。</li>
 * </ul>
 * <h2>特别的</h2>
 * <ol>
 * <li>task成员的执行顺序是添加时task数组中的顺序</li>
 * <li>在某一个task成员失败结束后，该task组中后续的task不会再被启动</li>
 * <li>整个task组的result与exception是最后一次执行的task的结果，不做任何修改
 * {@link #onAnyTaskComplete(HttpResult, Exception)}</li>
 * <li>整个task组的result与exception是最后一次执行的task的结果，不做任何修改
 * {@link #onAnyTaskComplete(HttpResult, Exception)}</li>
 * <li>task组的onComplete方法一定在task成员的onComplete方法之后执行</li>
 * </ol>
 * <p>
 * <b>注:如果请求失败，应该考虑某些task在接口端是不能被多次重复请求的</b><br/><br/>
 * <p>
 * <i>
 * 例:<br/>
 * SyncTasksGroup tasksGroup = new SyncTasksGroup(getContext(), mainListener);<br/>
 * tasksGroup.setTasks(task1, task2, task3);<br/>
 * tasksGroup.execute();<br/>
 * </i>
 * <p>
 * 作者：杨健
 * 时间：2016/12/26.
 */

public class SyncTasksGroup extends HQHttpRequest {
    private List<HQHttpRequest> hqHttpRequests = new ArrayList<>();
    private List<TaskListener> taskListeners = new ArrayList<>();
    private int executedTasksCount = 0;

    public SyncTasksGroup setTasks(HQHttpRequest... requests) {
        if (BuildConfig.DEBUG) {
            if (requests.length <= 0) {
                Assert.assertTrue("注意任务列表不要为空,如果为空会提前complete所有任务", false);
            }
            for (HQHttpRequest request : requests) {
                Assert.assertNotNull("具体子任务是不能为空,如果为空会忽略掉该任务", request);
            }
        }
        for (HQHttpRequest request : requests) {
            if (request != null) {
                hqHttpRequests.add(request);
                taskListeners.add(request.getTaskListener());
                request.setTaskListener(new TaskListener<HttpResult>() {
                    @Override
                    public void onTaskStart(TaskListener listener) {
                        //Empty
                    }

                    @Override
                    public void onTaskComplete(TaskListener listener,
                                               HttpResult result, Exception e) {
                        onAnyTaskComplete(result, e);
                    }

                    @Override
                    public void onCancel() {
                        //Empty
                    }
                });
            }
        }
        return this;
    }

    public SyncTasksGroup(Context context, TaskListener mainListener) {
        super(context, mainListener, HttpResult.class);
        this.status = TaskStatus.INIT;
    }

    @Override
    public void execute() {
        this.execute(true);
    }

    @Override
    public void execute(boolean savedToStack) {
        boolean starAble = onStarted();
        if (!starAble) throw new IllegalStateException("不要重复启动同一个任务");
        saveToStack(ctx);
        checkRequestsListNotNull();
        // 任务列表为空，提前结束任务
        if (hqHttpRequests.isEmpty()) {
            final int errorCode = 500;
            HQException lastException = new HQException(errorCode, "任务队列没有任务");
            onCompleted(null, lastException);
            return;
        }
        int firstTaskIndex = 0;
        executedTasksCount = firstTaskIndex;
        executeNextTask(firstTaskIndex);
    }

    private synchronized void onAnyTaskComplete(HttpResult lastResult, Exception lastException) {
        switch (getStatus()) {
            case CANCELED:
            case COMPLETED:
                return;
            default:
                break;
        }

        executedTasksCount++;
        int currentTaskIndex = executedTasksCount - 1;
        int nextTaskIndex = executedTasksCount;

        TaskListener listener = taskListeners.get(currentTaskIndex);
        if (listener != null) listener.onTaskComplete(listener, lastResult, lastException);

        if (lastResult.isSuccess(lastResult)) {
            if (isAllTasksComplete()) {
                super.onCompleted(lastResult, lastException);
            } else {
                executeNextTask(nextTaskIndex);
            }
        } else {
            // 提前结束任务组
            super.onCompleted(lastResult, lastException);
        }
    }

    private synchronized boolean isAllTasksComplete() {
        return executedTasksCount >= hqHttpRequests.size();
    }

    @Override
    protected boolean onCanceled() {
        boolean cancelAble = super.onCanceled();
        if (!cancelAble) return false;
        // 取消所有子任务
        for (TaskListener listener : taskListeners) {
            if (listener != null) listener.onCancel();
        }
        return true;
    }

    private final void executeNextTask(int taskIndex) {
        hqHttpRequests.get(taskIndex).execute(false);
    }

    private void checkRequestsListNotNull() {
        if (BuildConfig.DEBUG && (hqHttpRequests == null || hqHttpRequests.isEmpty())) {
            Assert.assertTrue("注意任务列表不能为空", false);
        }
    }

    // 以下方法什么也不要做,也不会被调用,只是为了完成重写,统一项目中的任务类型
    @Override
    protected final String getHost() {
        return null;
    }

    @Override
    protected final void addParam(List list) {
        //Empty
    }

    @Override
    protected final String getPath() {
        return null;
    }
}
