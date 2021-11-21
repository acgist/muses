package com.acgist.redis.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.acgist.redis.SerializeService;

@Configuration
@EnableCaching
public class CacheConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		LOGGER.info("配置CacheManager");
		final RedisCacheConfiguration config = RedisCacheConfiguration
			.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(SerializeService.buildKeySerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(SerializeService.buildValueSerializer()))
			// 禁止缓存空值
//			.disableCachingNullValues()
			// 设置过期时间
			.entryTtl(Duration.ofMinutes(30));
		final RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
			.cacheDefaults(config)
			.build();
		return cacheManager;
	}

}
