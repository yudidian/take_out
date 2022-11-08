package com.dhy.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.dhy.common.R;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig{
    /**
     * redisTemplate相关
     */
    @Bean("customRedisTemplate")
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * 选择redis作为默认缓存工具
     */
    @Bean("redisCacheManager")
    @Primary
    @DependsOn("customRedisTemplate")
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisTemplate.getConnectionFactory())
            // 设置缓存默认永不过期
            .cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                    // 不缓存null(需要与unless = "#result == null"共同使用)
                    .disableCachingNullValues()
                    .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer())))
            // 配置同步修改或删除 put/evict
            .transactionAware()
            .build();
    }
}
