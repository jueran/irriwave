package com.irrigate.base.list.baselist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.salted.core.core.net.HttpResult;
import com.salted.core.core.net.TaskListener;
import com.salted.core.core.net.executor.ResultConverter;
import com.salted.core.core.net.executor.TaskExecutor;
import com.salted.core.core.net.executor.TaskPair;
import com.salted.core.core.util.ListUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/13.
 */

@SuppressWarnings("PMD.ModifiedCyclomaticComplexity")
public class BaseListFragmentModel implements BaseListFragmentContract.Model,
        OnListApiPagingStateConfirmedCallback {
    private static final String START_VALUE = "0";
    private static final String COUNT_EACH_PAGE = "20";

    private final Context context;
    private final BaseTaskPair listTask;
    private final ParamsProvider paramsProvider;
    private final List<BaseTaskPair> otherTaskSet;

    private boolean isLoadingAll;
    private boolean isLoadingMore;

    //是否分页
    private boolean isListPagingEnable = true;

    private int currentItemsCount = 0;

    public BaseListFragmentModel(Context context,
                                 BaseTaskPair listTaskClazz,
                                 List<BaseTaskPair> otherTaskClazzSet,
                                 ParamsProvider paramsProvider) {
        this.context = context;
        this.listTask = listTaskClazz;
        this.otherTaskSet = otherTaskClazzSet;
        this.paramsProvider = paramsProvider;
    }

    @Override
    public boolean isPagingEnable() {
        return isListPagingEnable;
    }

    @Override
    public void loadAll(final OnItemsLoadedListener onItemsLoadedListener) {
        if (isLoadingMore) {
            onItemsLoadedListener.onCancel();
            return;
        }
        isLoadingAll = true;

        List<TaskPair> taskList = getTaskPairs();

        TaskExecutor executor = new TaskExecutor(new TaskExecutor.Callback() {
            @Override
            public void onSuccess(Map<String, Object> results) {
                isLoadingAll = false;

                Object value = results.get(listTask.getRequestClass().getName());
                if (value == null) {
                    onItemsLoadedListener.onEmpty(results);
                }
                if (!(value instanceof List)) {
                    throw new IllegalStateException("Your words task must return a List type"
                            + "in result!");
                }
                List objects = (List) value;
                currentItemsCount = objects.size();
                if (currentItemsCount == 0) {
                    onItemsLoadedListener.onEmpty(results);
                } else {
                    onItemsLoadedListener.onSuccess(objects, results);
                }
            }

            @Override
            public void onError() {
                isLoadingAll = false;
                onItemsLoadedListener.onError();
            }

            @Override
            public void onException(List<Exception> exceptions) {
                isLoadingAll = false;
                onItemsLoadedListener.onException(exceptions.get(0));
            }
        }, taskList.toArray(new TaskPair[]{}));

        if (taskList != null && !taskList.isEmpty()) {
            onItemsLoadedListener.onStart();
            executor.execute();
        } else {
            onItemsLoadedListener.onCancel();
        }
    }

    @NonNull
    private List<TaskPair> getTaskPairs() {
        List<TaskPair> taskList = new ArrayList<>();

        Map<String, Object> params = paramsProvider.getParams();
        params.put(BaseListTask.START, START_VALUE);
        params.put(BaseListTask.COUNT, COUNT_EACH_PAGE);

        fillTaskPair(taskList, getListTask(params));

        if (otherTaskSet != null && !otherTaskSet.isEmpty()) {
            for (BaseTaskPair pair : otherTaskSet) {
                fillTaskPair(taskList, buildTask(pair, params));
            }
        }
        return taskList;
    }

    private void fillTaskPair(List<TaskPair> taskList, BaseListTask task) {
        if (task != null) {
            TaskPair taskPair = new TaskPair(new ResultConverter() {
                @Override
                public Object convert(Object result) {
                    return ((Converter) result).convert();
                }
            }, task);
            taskList.add(taskPair);
        }
    }

    @Nullable
    private BaseListTask getListTask(Map<String, Object> params) {
        BaseListTask baseListTask = buildTask(listTask, params);
        if (baseListTask != null) {
            baseListTask.setOnListApiPagingStateConfirmedCallback(this);
        }
        return baseListTask;
    }

    @Nullable
    private BaseListTask buildTask(BaseTaskPair taskPair,
                                   Map<String, Object> params) {
        if (taskPair == null) {
            return null;
        }
        BaseListTask task = null;
        try {
            Constructor constructor = taskPair.getRequestClass().getConstructors()[0];
            Class[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length < 3) {
                throw new IllegalStateException("Task constructor's parameters must have types:"
                        + " Context,Class<HttpResult>,Map");
            }
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = null;
            }
            for (int i = 0; i < parameterTypes.length; i++) {
                Class clazz = parameterTypes[i];
                if (clazz.isAssignableFrom(Context.class)) {
                    parameters[i] = context;
                } else if (clazz.isAssignableFrom(Map.class)) {
                    parameters[i] = params;
                } else if (clazz.isAssignableFrom(Class.class)) {
                    parameters[i] = taskPair.getResultClass();
                }
            }

            task = (BaseListTask) constructor.newInstance(parameters);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return task;
    }

    @Override
    public void loadMore(final OnItemsLoadedListener onMoreLoadedListener) {
        if (isLoadingAll) {
            onMoreLoadedListener.onCancel();
            return;
        }
        isLoadingMore = true;

        if (!isListPagingEnable) {
            onMoreLoadedListener.onEmpty(null);
            return;
        }

        Map<String, Object> params = paramsProvider.getParams();
        params.put(BaseListTask.START, "" + currentItemsCount);
        params.put(BaseListTask.COUNT, COUNT_EACH_PAGE);

        BaseListTask task = getListTask(params);
        if (task == null) {
            onMoreLoadedListener.onError();
            return;
        }

        task.setTaskListener(new TaskListener<HttpResult>() {
            @Override
            public void onTaskStart(TaskListener listener) {
                onMoreLoadedListener.onStart();
            }

            @Override
            public void onTaskComplete(TaskListener listener,
                                       final HttpResult result, final Exception e) {
                isLoadingMore = false;

                if (isException(result, e, onMoreLoadedListener)) return;

                List items = (List) ((Converter) result).convert();
                if (ListUtil.isEmpty(items)) {
                    onMoreLoadedListener.onEmpty(null);
                    return;
                }

                onMoreLoadedListener.onSuccess(items, null);
                currentItemsCount += items.size();
            }

            @Override
            public void onCancel() {
                onMoreLoadedListener.onCancel();
                isLoadingMore = false;
            }
        });
        task.execute();
    }

    private boolean isException(HttpResult result, Exception e,
                                OnItemsLoadedListener onMoreLoadedListener) {
        if (result == null) {
            onMoreLoadedListener.onError();
            return true;
        }
        if (!result.isSuccess()) {
            onMoreLoadedListener.onException(e);
            return true;
        }
        if (!(result instanceof BaseListResult)) {
            throw new ClassCastException("Your HttpResult must extends BaseListResult!");
        }
        return false;
    }

    @Override
    public void onConfirmed(boolean pagingEnabled) {
        isListPagingEnable = pagingEnabled;
    }
}
