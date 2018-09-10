package com.wudy.timer.config;

import com.wudy.timer.cache.CacheManager;
import com.wudy.timer.cache.CacheMapManager;
import com.wudy.timer.cache.RedisCacheManager;
import com.wudy.timer.cache.RedisCacheMapManager;
import com.wudy.timer.serialize.HessianRedisSerializer;
import com.wudy.timer.serialize.JacksonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 这是一个坑：http://blog.csdn.net/u014481096/article/details/54134904
 */
@Configuration
public class RedisConfig {
    /**
     * 基于Jackson技术的序列化器。
     * 实际上是将对象转换成JSON字符串，可读性强，对用户友好。
     * 但是需要指定序列化对象的类型，通用性不强。
     * 如下指定的是Object超类，对于一些复杂对象（对象嵌套），
     * 最后反序列化出来的会是LinkedHashMap类型，导致类型转换出错。
     *
     * @return JacksonRedisSerializer
     */
    @Bean(name = "jacksonRedisSerializer")
    public RedisSerializer getJacksonSerializer() {

        return new JacksonRedisSerializer(Object.class);
    }

    /**
     * 基于Hessian技术的序列化器，可读性不强，但是通用，
     * 且性能较好，适合做数据传输。
     *
     * @return HessianRedisSerializer
     */
    @Bean(name = "hessianRedisSerializer")
    public RedisSerializer getHessianSerializer() {
        return new HessianRedisSerializer();
    }


    /**
     * 依赖taxi-common的module如有需要，可以对RedisTemplate进行相关定制，默认使用如下RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        //使用hessian序列化较为通用
        template.setHashValueSerializer(getHessianSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(CacheMapManager.class)
    public CacheMapManager getCacheMapManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheMapManager manager = new RedisCacheMapManager();
        manager.setRedisTemplate(redisTemplate);
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager getCacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheManager manager = new RedisCacheManager();
        manager.setRedisTemplate(redisTemplate);
        return manager;
    }
}
