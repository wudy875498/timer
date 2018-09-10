package com.wudy.timer.cache;

import java.util.Date;
import java.util.Set;

/**
 * 基础缓存接口，key的类型一律是String
 * @param <V> 要缓存的值对象类型
 */
public interface CacheManager<V> {

    V get(String cacheKey);

    void set(String cacheKey, V val, long seconds);

    void set(String cacheKey, V val);

    void delete(String cacheKey);

    void rename(String oldKey, String newKey);

    boolean exist(String cacheKey);

    long incrBy(String key, long delta);

    Set<String> keys(String pattern);

    boolean expire(String cacheKey, long seconds);

    boolean expireAt(String cacheKey, long timestamp);

    boolean expireAt(String cacheKey, Date expireDate);

    long ttl(String cacheKey);
}
