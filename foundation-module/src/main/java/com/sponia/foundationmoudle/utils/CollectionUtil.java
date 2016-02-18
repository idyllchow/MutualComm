package com.sponia.foundationmoudle.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author shibo
 * @packageName com.sponia.foundationmoudle.utils
 * @description 集合类操作
 * @date 15/10/14
 */
public class CollectionUtil {

    /**
     * 不允许实例化
     */
    private CollectionUtil() {
    }

    /**
     * 获取两个集合的不同元素
     *
     * @param collmax
     * @param collmin
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Collection getDiffent(Collection collmax, Collection collmin) {
        Collection csReturn = new ArrayList();
        Collection max = collmax;
        Collection min = collmin;
        //先比较大小,这样会减少后续map的if判断次数
        if (collmax.size() < collmin.size()) {
            max = collmin;
            min = collmax;
        }
        //直接指定大小,防止再散列
        Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
        for (Object object : max) {
            map.put(object, 1);
        }
        for (Object object : min) {
            if (map.get(object) == null) {
                csReturn.add(object);//把集合2与集合1不同的元素添加到csReturn集合
            } else {
                map.put(object, 2);//两个集合相同的元素的值变为2
            }
        }
        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                csReturn.add(entry.getKey());//把集合1与集合2不同的元素添加到csReturn集合
            }
        }
        return csReturn;
    }

    /**
     * 获取两个集合的不同元素,去除重复
     *
     * @param collmax
     * @param collmin
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Collection getDiffentNoDuplicate(Collection collmax, Collection collmin) {
        return new HashSet(getDiffent(collmax, collmin));//两个集合中本来可能存在重复元素,放进set集合后达到去除重复的目的
    }


}
