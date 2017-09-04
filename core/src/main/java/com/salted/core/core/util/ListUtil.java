package com.salted.core.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinyuanzhong on 2017/4/11.
 */

public final class ListUtil {
    private ListUtil() {
    }

    public static List getNonNullList(List list) {
        if (list == null) {
            return new ArrayList();
        }
        return list;
    }

    /**
     * 判断两个不重复的list是否相等
     *
     * @param objects1
     * @param objects2
     * @return
     */
    public static boolean isTwoListEquals(List<?> objects1, List<?> objects2) {
        if (objects1 == null || objects2 == null) {
            return false;
        }
        if (objects1.size() != objects2.size()) {
            return false;
        }
        for (Object o : objects1) {
            if (!objects2.contains(o)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(List<?> ids) {
        return getNonNullList(ids).isEmpty();
    }
}
