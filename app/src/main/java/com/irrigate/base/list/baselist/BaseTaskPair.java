package com.irrigate.base.list.baselist;

/**
 * Created by xinyuanzhong on 2017/6/16.
 */

public class BaseTaskPair {
    private final Class<? extends BaseListTask> requestClass;
    private final Class<? extends BaseListResult> resultClass;

    public BaseTaskPair(Class<? extends BaseListTask> requestClass,
                        Class<? extends BaseListResult> resultClass) {
        this.requestClass = requestClass;
        this.resultClass = resultClass;
    }

    public Class<? extends BaseListTask> getRequestClass() {
        return requestClass;
    }

    public Class<? extends BaseListResult> getResultClass() {
        return resultClass;
    }
}
