package com.irrigate.base.list.baselist;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyuanzhong on 2017/6/14.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter {
    protected static final int TYPE_HEADER = 1000;
    protected static final int TYPE_ITEM = 1001;

    private Map<String, Object> dataMap = new HashMap<>();

    protected List<T> items = new ArrayList<>();

    public final List<T> getItems() {
        return items;
    }

    public final void setItems(List<T> items) {
        if (items != null) {
            this.items.clear();
            this.items.addAll(items);
        }
    }

    public final Map<String, Object> getDataMap() {
        return dataMap;
    }

    public final void setDataMap(Map<String, Object> dataMap) {
        if (dataMap != null) {
            this.dataMap.clear();
            this.dataMap.putAll(dataMap);
        }
    }

    public final Object getResultByTask(Class clazz) {
        return dataMap.get(clazz.getName());
    }
}
