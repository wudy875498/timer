package com.wudy.timer.serialize;

import com.wudy.timer.utils.SerializeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Slf4j
public class HessianRedisSerializer<T> implements RedisSerializer<T> {

    /**
     * Serialize the given object to binary data.
     *
     * @param t object to serialize
     * @return the equivalent binary data
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {

        return SerializeUtils.serialize(t);
    }

    /**
     * Deserialize an object from the given binary data.
     *
     * @param bytes object binary representation
     * @return the equivalent object instance
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        Object o = SerializeUtils.deserialize(bytes);
        if (o != null) {
            return (T) o;
        }
        return null;
    }
}
