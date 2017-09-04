package com.irrigate.core.net.executor;


import com.irrigate.core.net.HQHttpRequest;

/**
 * Created by xinyuanzhong on 2017/4/5.
 * <p>
 * 任务包装类,是单向链表结构。包含了转化类和真正的Request
 */
public class TaskPair {
    public ResultConverter converter;

    public HQHttpRequest request;
    /**
     * 阻止执行
     */
    private boolean block;

    private TaskPair next;

    private boolean executeLater;

    public boolean isExecuteLater() {
        return executeLater;
    }

    public void setExecuteLater(boolean executeLater) {
        this.executeLater = executeLater;
    }

    public TaskPair getNext() {
        return next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public void setNext(TaskPair next) {
        this.next = next;
        next.setExecuteLater(true);
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }


    public TaskPair(ResultConverter converter, HQHttpRequest request) {
        this.converter = converter;
        this.request = request;
    }
}
