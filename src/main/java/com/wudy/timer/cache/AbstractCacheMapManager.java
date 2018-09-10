package com.wudy.timer.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractCacheMapManager<HV> implements CacheMapManager<Map<String, HV>, HV> {

    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, HV> ops;

    /**
     * 判断Map中是否有某个键
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map键
     */
    @Override
    public boolean keyExist(String cacheKey, String hashKey) {
        Boolean exist = getHashOperations().hasKey(cacheKey, hashKey);
        return exist != null && exist;
    }

    /**
     * 获取整个Map
     *
     * @param cacheKey 缓存的键
     */
    @Override
    public Map<String, HV> getMap(String cacheKey) {

        return toBean(getHashOperations().entries(cacheKey));
    }

    /**
     * 获取Map中的某个值
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map键
     */
    @Override
    public HV getMapValue(String cacheKey, String hashKey) {

        return getHashOperations().get(cacheKey, hashKey);
    }

    /**
     * 为map添加键值对
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map的键
     * @param v        map的值
     */
    @Override
    public void put(String cacheKey, String hashKey, HV v) {

        getHashOperations().put(cacheKey, hashKey, v);
    }

    /**
     * 将一个map一次性缓存下来
     *
     * @param cacheKey 缓存的键
     * @param v        缓存的数据，最终会转换成map对象
     */
    @Override
    public void putAll(String cacheKey, Map<String, HV> v) {

        getHashOperations().putAll(cacheKey, v);
    }

    /**
     * map的increase操作
     *
     * @param cacheKey 缓存的键
     * @param hashKey  map的键
     * @param delta    增量
     * @return 增加后的值
     */
    @Override
    public long hincrby(String cacheKey, String hashKey, long delta) {
        Long increment = getHashOperations().increment(cacheKey, hashKey, delta);
        return increment == null ? -1 : increment;
    }

    /**
     * 获取Map的所有key
     *
     * @param cacheKey 缓存的键
     */
    @Override
    public Set<?> mapKeys(String cacheKey) {
        return getHashOperations().keys(cacheKey);
    }

    /**
     * 删除Map元素
     *
     * @param cacheKey 缓存的键
     * @param hashKeys map的键
     */
    @Override
    public void deleteMap(String cacheKey, String... hashKeys) {
        getHashOperations().delete(cacheKey, hashKeys);
    }

    /**
     * 获取Map元素个数
     *
     * @param cacheKey 缓存的键
     */
    @Override
    public long getMapSize(String cacheKey) {
        Long size = getHashOperations().size(cacheKey);
        return size == null ? 0 : size;
    }

    protected HashOperations<String, String, HV> getHashOperations() {
        if (ops == null) {
            ops = redisTemplate.opsForHash();
        }
        return ops;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
