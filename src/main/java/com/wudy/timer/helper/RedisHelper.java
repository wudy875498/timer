package com.wudy.timer.helper;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.wudy.timer.cache.CacheManager;
import com.wudy.timer.cache.CacheMapManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 * redis 帮助类
 *
 * @author Eric Lau
 * @Date 2017/8/31
 */
@Slf4j
@Component
public class RedisHelper {

    @Autowired
    private CacheMapManager<Map<String, Object>, Object> cacheMapManager;

    @Autowired
    private CacheManager<Object> cacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取缓存内容
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return cacheManager.get(key);
    }

    public RedisConnectionFactory getConnectionFactory() {
        return redisTemplate.getConnectionFactory();
    }

    /**
     * 根据 key 获取对应的 value
     *
     * @param key           键，不可为null
     * @param defaultVal    如果找不到，返回默认值
     * @param setIfNull     如果key不存在，是否创建 {@Code <key, defaultVal> }
     * @param expireSeconds 缓存过期时间，只有get的值不为null，setIfNull=true时，此参数生效
     * @return 获取的value
     */
    public Object get(String key, Object defaultVal, boolean setIfNull, int expireSeconds) {

        if (StringUtils.isBlank(key)) {
            return null;
        }
        Object value = get(key);
        if (value == null) {
            if (setIfNull) {
                set(key, defaultVal, expireSeconds);
                return defaultVal;
            }
            return null;
        }
        return value;
    }

    /**
     * 根据 key 获取对应的 value
     *
     * @param key        键，不可为null
     * @param defaultVal 如果找不到，返回默认值
     * @param setIfNull  如果key不存在，是否创建 {@Code <key, defaultVal> }
     * @return 获取的value
     */
    public Object get(String key, Object defaultVal, boolean setIfNull) {

        if (StringUtils.isBlank(key)) {
            return null;
        }
        Object value = get(key);
        if (value == null) {
            if (setIfNull) {
                set(key, defaultVal);
                return defaultVal;
            }
            return null;
        }
        return value;
    }

    /**
     * 设置缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public void set(String key, Object value) {
        cacheManager.set(key, value);
    }

    /**
     * 设置缓存，并指定超时时间
     *
     * @param key           键
     * @param value         值
     * @param expirySeconds 过期时间，单位：秒
     */
    public void set(String key, Object value, long expirySeconds) {
        cacheManager.set(key, value, expirySeconds);
    }

    /**
     * 删除缓存内容
     *
     * @param key 键
     */
    public void del(String key) {
        cacheManager.delete(key);
    }

    /**
     * 按照格式获取key
     *
     * @param pattern 正则
     */
    public Set<String> keys(String pattern) {
        if (StringUtils.isBlank(pattern)) return null;
        return cacheManager.keys(pattern);
    }

    /**
     * 判断key是否存在
     */
    public boolean exist(String cacheKey) {
        return !StringUtils.isBlank(cacheKey) && cacheManager.exist(cacheKey);
    }

    /**
     * 更改key的名称
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     */
    public void rename(String oldKey, String newKey) {
        cacheManager.rename(oldKey, newKey);
    }

    /**
     * 自增原子操作
     *
     * @param key   键
     * @param delta 递增量
     * @return 递增后的值
     */
    public Long incrBy(String key, long delta) {
        return cacheManager.incrBy(key, delta);
    }

    /**
     * +1操作
     *
     * @param key 键
     * @return +1后的值
     */
    public Long incrBy(String key) {
        return cacheManager.incrBy(key, 1);
    }

    /**
     * 获取Map缓存
     *
     * @param redisKey redis存储Map数据的key
     * @return Map结构的缓存对象
     */
    public Map<String, Object> getMap(String redisKey) {
        return cacheMapManager.getMap(redisKey);
    }

    /**
     * 添加Map的值
     *
     * @param redisKey redis存储Map数据的key
     * @param hashKey  Map数据本身的hash key
     * @param value    存入Map中的值
     */
    public void put(String redisKey, String hashKey, Object value) {
        cacheMapManager.put(redisKey, hashKey, value);
    }

    /**
     * 获取Map中的数据
     *
     * @param redisKey redis存储Map数据的key
     * @param hashKey  Map数据本身的hash key
     * @return hashKey对应的值
     */
    public Object getMapValue(String redisKey, String hashKey) {

        return cacheMapManager.getMapValue(redisKey, hashKey);
    }

    /**
     * HINCRBY操作
     */
    public long hincrBy(String redisKey, String hashKey, long delta) {
        return cacheMapManager.hincrby(redisKey, hashKey, delta);
    }

    /**
     * HashValue+1操作
     */
    public long hincrBy(String redisKey, String hashKey) {
        return cacheMapManager.hincrby(redisKey, hashKey, 1);
    }

    /**
     * 获取Map的所有key
     *
     * @param redisKey redis缓存的key名称
     * @return Map的所有key
     */
    public Set<?> mapKeys(String redisKey) {

        return cacheMapManager.mapKeys(redisKey);
    }

    /**
     * 设置Map缓存
     *
     * @param redisKey redis缓存的key名称
     * @param map      要缓存的Map对象
     */
    public void setMap(String redisKey, Map<String, Object> map) {
        cacheMapManager.putAll(redisKey, map);
    }

    /**
     * 删除Map元素
     *
     * @param redisKey redis存储Map数据的key
     * @param hashKeys Map数据本身的hash redisKey
     */
    public void deleteMap(String redisKey, String... hashKeys) {
        cacheMapManager.deleteMap(redisKey, hashKeys);
    }

    /**
     * 获取Map元素个数
     *
     * @param redisKey redis存储Map数据的key
     * @return Map元素个数
     */
    public long getMapSize(String redisKey) {
        return cacheMapManager.getMapSize(redisKey);
    }


    /**
     * 缓存对象
     *
     * @param key
     * @param value
     */
    public void setObject(String key, Object value) {
        setObject(key, value, -1);
    }

    /**
     * 设置缓存（复杂对象）
     *
     * @param key          键
     * @param value        值
     * @param cacheSeconds 超时时间（单位：s），0为不超时
     */
    public void setObject(final String key, final Object value, long cacheSeconds) {
        final long liveTime = cacheSeconds;

        redisTemplate.execute((RedisCallback<Long>) connection -> {
            ByteArrayOutputStream os;
            Hessian2Output output = null;
            try {
                byte[] keyBytes = key.getBytes();
                os = new ByteArrayOutputStream();
                output = new Hessian2Output(os);
                output.setCloseStreamOnClose(true);
                output.writeObject(value);
                output.flush();
                byte[] bytes = os.toByteArray();
                connection.set(keyBytes, bytes);
                if (liveTime > 0) {
                    connection.expire(keyBytes, liveTime);
                }
                return 1L;
            } catch (IOException e) {
                log.error("[Hessian2]对象序列化失败, {}", e);
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    log.error("输出流关闭异常, {}", e);
                }
            }
            return 0L;
        });
    }

    /**
     * 获取缓存（复杂对象）
     *
     * @param key 键
     * @return 通常为自定义类
     * @see #get(String)
     */
    public Object getObject(final String key) {
        return redisTemplate.execute((RedisConnection connection) -> {
            ByteArrayInputStream is;
            Hessian2Input input = null;
            try {
                byte[] value = connection.get(key.getBytes());
                if (value == null) {
                    return null;
                }
                is = new ByteArrayInputStream(value);
                input = new Hessian2Input(is);
                input.setCloseStreamOnClose(true);
                return input.readObject();
            } catch (IOException e) {
                log.error("[Hessian2]对象反序列化失败, {}", e);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    log.error("输入流关闭异常, {}", e);
                }
            }
            return null;
        });
    }
}
