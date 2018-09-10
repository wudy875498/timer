package com.wudy.timer.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.text.DateFormat;

/**
 * 基于Jackson的序列化工具
 */
@Slf4j
public class JacksonRedisSerializer<T> implements RedisSerializer<T> {

    private Class<T> clazz;

    public JacksonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 对象序列化，Object转换成字节码
     * @param t Object
     * @return 对象被序列化成的字节码数组
     * @throws SerializationException 序列化异常
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(DateFormat.getDateTimeInstance());
            return mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常,{}", e);
        }
        return new byte[0];
    }

    /**
     * 反序列化，相当于字节码转换成对应的Object
     * @param bytes 需要反序列化的字节码数组
     * @return Object
     * @throws SerializationException 反序列化异常
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(DateFormat.getDateTimeInstance());
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("JSON解析异常,{}", e);
        }
        return null;
    }

}
