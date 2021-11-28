package com.acgist.redis.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.acgist.common.JSONUtils;
import com.acgist.common.config.CommonAutoConfiguration.SerializerType;

@Configuration
@EnableCaching
@ConditionalOnClass({CacheManager.class, RedisTemplate.class})
public class RedisAutoConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisAutoConfiguration.class);

	@Value("${system.cache.prefix:cache_}")
	private String cachePrefix;
	
	@Autowired
	private SerializerType serializerType;
	
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		LOGGER.info("配置CacheManager");
		final RedisCacheConfiguration config = RedisCacheConfiguration
			.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(this.buildKeySerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.buildValueSerializer()))
			// 禁止缓存空值
//			.disableCachingNullValues()
			// 设置缓存前缀
			.prefixCacheNameWith(this.cachePrefix)
			// 设置过期时间
			.entryTtl(Duration.ofMinutes(30));
		final RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
			.cacheDefaults(config)
			.build();
		return cacheManager;
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		LOGGER.info("配置RedisTemplate");
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(this.buildKeySerializer());
		template.setValueSerializer(this.buildValueSerializer());
		template.afterPropertiesSet();
		return template;
	}
	
	public RedisSerializer<String> buildKeySerializer() {
		return StringRedisSerializer.UTF_8;
	}
	
	public RedisSerializer<?> buildValueSerializer() {
		if(this.serializerType == SerializerType.JACKSON) {
			final Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
			serializer.setObjectMapper(JSONUtils.buildSerializeMapper());
			return serializer;
		} else {
			return new JdkSerializationRedisSerializer();
		}
	}
	
}
