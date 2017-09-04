package com.irrigate.core.net.executor;

import android.util.Log;

import com.irrigate.BuildConfig;
import com.irrigate.core.net.HttpResult;
import com.irrigate.core.net.TaskListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xinyuanzhong on 2017/4/1.
 * <p>
 * 多个任务执行器
 * <p>
 * NOTE:有并行、串行和混合执行3种模式
 * 1:并行。构造方法中传入全部构造好的需要执行的TaskPair，全部任务执行成功后会得到一个Map<String, Object>,
 * 从Map中通过Task的ClassName取值，强转成所需要的类。
 * 2:串行。使用会比并行复杂一点，如果是前后任务没有关系，只是规定顺序的话只需要pair1.setNext(pair2)就好了；
 * 如果后面Task2的参数需要用到前面Task1的结果，那么使用的时候还需要注意：TaskPair2的创建要在TaskPair1之前，
 * 因为TaskPair1返回的时候需要修改TaskPair2的Request，修改Request在TaskPair1的convert方法中进行。
 * 3:混合。就是在串行上在加一个并行，只需要注意串行Pair的构造过程。
 */
@SuppressWarnings("PMD.ModifiedCyclomaticComplexity")
public class TaskExecutor {
    protected int remainTaskCount;

    protected boolean taskError;

    protected List<TaskPair> pairs = new ArrayList<>();

    protected Map<String, Object> results = new ConcurrentHashMap<>();

    protected List<Exception> exceptions = new ArrayList<>();

    protected Callback callback;
    private boolean isCanceled;

    public TaskExecutor(Callback callback, TaskPair... pairs) {
        Collections.addAll(this.pairs, pairs);
        remainTaskCount = pairs.length;
        this.callback = callback;
    }

    public void execute() {
        taskError = false;
        remainTaskCount = pairs.size();
        results.clear();
        exceptions.clear();

        for (final TaskPair pair : pairs) {
            //被主动cancel掉，直接返回
            if (isCanceled) {
                onAllTaskComplete();
                isCanceled = false;
                break;
            }

            //如果是串行任务的非首个任务，不执行
            if (pair != null && pair.isExecuteLater()) {
                continue;
            }
            performExecute(pair);
        }
    }

    private void performExecute(final TaskPair pair) {
        if (pair == null) {
            onSingleTaskComplete();
            return;
        }
        //如果被阻塞或者Request为空则停止  并且停止后面所有的请求
        final TaskPair next = pair.getNext();
        if (pair.isBlock() || pair.request == null) {
            if (pair.hasNext()) {
                next.setBlock(true);
                performExecute(next);
            }
            onSingleTaskComplete();
            return;
        }

        pair.request.setTaskListener(new TaskListener<HttpResult>() {
            @Override
            public void onTaskStart(TaskListener listener) {
                //Empty
            }

            @Override
            public void onTaskComplete(TaskListener listener, HttpResult result, Exception e) {
                if (result != null) {
                    if (result.isSuccess()) {
                        Object value = pair.converter.convert(result);
                        if (value != null) {
                            results.put(pair.request.getClass().getName(), value);
                        }
                    } else {
                        exceptions.add(e);
                    }
                } else {
                    taskError = true;
                }
                //如果有串行任务，继续执行
                if (pair.hasNext()) {
                    performExecute(next);
                }
                onSingleTaskComplete();
            }

            @Override
            public void onCancel() {
                isCanceled = true;
                exceptions.add(new Exception(""));
            }
        });
        pair.request.execute();
    }

    protected synchronized void onSingleTaskComplete() {
        remainTaskCount--;
        if (BuildConfig.DEBUG) Log.d("TaskExecutor", "Task Total Count: "
                + pairs.size() + ", RemainCount: " + remainTaskCount);
        if (remainTaskCount == 0) {
            onAllTaskComplete();
        }
    }

    private void onAllTaskComplete() {
        if (taskError) {
            callback.onError();
        } else if (!exceptions.isEmpty()) {
            callback.onException(exceptions);
        } else {
            callback.onSuccess(results);
        }
    }

    public interface Callback {
        void onSuccess(Map<String, Object> results);

        void onError();

        void onException(List<Exception> exceptions);
    }
}
