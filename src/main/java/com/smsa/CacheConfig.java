package com.smsa;

//package com.smsa.smsa;
//
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.cache.interceptor.KeyGenerator;
//
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import java.lang.reflect.Method;
//import java.time.Duration;
//import java.util.Arrays;
//
//@Configuration
//@EnableCaching  // 🔴 Important: Enables Spring Cache support
//public class CacheConfig {
//
//    // 🔸 Set default TTL to 10 minutes
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(config)
//                .build();
//    }
//
//    // 🔸 Custom Key Generator (optional)
//    @Bean("customKeyGenerator")
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                return method.getName() + "_" + Arrays.deepHashCode(params);
//            }
//        };
//    }
//}
