package com.salted.core.core.net.executor;

/**
 * Created by xinyuanzhong on 2017/4/5.
 * <p>
 * API模型和客户端模型转化类
 */
public interface ResultConverter<R, M> {
    M convert(R r);
}
