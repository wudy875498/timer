package com.wudy.timer.cache;


import com.wudy.timer.utils.BeanRefUtils;

import java.util.Map;
import java.util.Set;

/**
 * 对Map类型数据的缓存接口，缓存的键类型是String类型，Map的键也是String类型。
 *
 * @param <V>  要缓存的数据，可以不是Map结构，但最终都会被转换成Map结构
 * @param <HV> Map结构中值的类型
 */
public interface CacheMapManager<V, HV> {

    /**
     * 判断Map中是否有某个键
     *ß
     * @param cacheKey 缓存的键
     * @param hashKey  map键
     */
    boolean keyExist(String cacheKey, String hashKey);

    /**
     * 获取整个Map
     *
     * @param cacheKey 缓存的键
     */
    V getMap(String cacheKey);

    /**
     * 获取Map中的某个值
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map键
     */
    HV getMapValue(String cacheKey, String hashKey);

    /**
     * 为map添加键值对
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map的键
     * @param HV       map的值
     */
    void put(String cacheKey, String hashKey, HV HV);

    /**
     * 将一个map一次性缓存下来
     *
     * @param cacheKey 缓存的键
     * @param v        缓存的数据，最终会转换成map对象
     */
    void putAll(String cacheKey, V v);

    /**
     * map的increase操作
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map的键
     * @param delta    增量
     * @return 增加后的值
     */
    long hincrby(String cacheKey, String hashKey, long delta);

    /**
     * 转换成map的接口
     *
     * @param v 需要被转换的对象实例
     */
    default Map<String, HV> toMap(V v) {
        return (Map<String, HV>) BeanRefUtils.toMap(v, true);
    }

    /**
     * 转换成对应的Bean
     *
     * @param map 待转换的map
     */
    V toBean(Map<String, HV> map);

    /**
     * 获取Map的所有key
     */
    Set<?> mapKeys(String cacheKey);

    /**
     * 删除Map元素
     */
    void deleteMap(String cacheKey, String... hashKeys);

    /**
     * 获取Map元素个数
     */
    long getMapSize(String cacheKey);

}
