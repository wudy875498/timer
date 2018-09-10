package com.wudy.timer.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisCacheManager implements CacheManager<Object> {

    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> ops;

    @Override
    public Object get(String cacheKey) {

        return getValueOperations().get(cacheKey);
    }

    @Override
    public void set(String cacheKey, Object val, long seconds) {
        getValueOperations().set(cacheKey, val, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void set(String cacheKey, Object val) {
        getValueOperations().set(cacheKey, val);
    }

    @Override
    public void delete(String cacheKey) {
        redisTemplate.delete(cacheKey);
    }

    @Override
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    @Override
    public boolean exist(String cacheKey) {
        return redisTemplate.hasKey(cacheKey);
    }

    @Override
    public long incrBy(String key, long delta) {
        return getValueOperations().increment(key, delta);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public boolean expire(String cacheKey, long seconds) {
        Boolean result = redisTemplate.expire(cacheKey, seconds, TimeUnit.SECONDS);
        return result == null ? false : result;
    }

    @Override
    public boolean expireAt(String cacheKey, long timestamp) {
        Boolean result = redisTemplate.expireAt(cacheKey, new Date(timestamp));
        return result == null ? false : result;
    }

    @Override
    public boolean expireAt(String cacheKey, Date expireDate) {
        Boolean result = redisTemplate.expireAt(cacheKey, expireDate);
        return result == null ? false : result;
    }

    @Override
    public long ttl(String cacheKey) {
        return redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
    }

    protected ValueOperations<String, Object> getValueOperations() {
        if (ops == null) {
            ops = redisTemplate.opsForValue();
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
