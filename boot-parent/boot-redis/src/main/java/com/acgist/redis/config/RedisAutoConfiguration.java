package com.acgist.redis.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

import com.acgist.boot.config.BootAutoConfiguration.SerializerType;
import com.acgist.boot.service.CacheService;
import com.acgist.boot.utils.JSONUtils;
import com.acgist.redis.service.impl.CacheServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis和缓存配置
 * 
 * @author acgist
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnClass({CacheManager.class, RedisTemplate.class})
@EnableConfigurationProperties(CacheProperties.class)
public class RedisAutoConfiguration {
	
	/**
	 * 缓存时间：分钟
	 */
	@Value("${system.cache.ttl:30}")
	private int cacheTtl;
	/**
	 * 缓存前缀
	 */
	@Value("${system.cache.prefix:cache::}")
	private String cachePrefix;
	
	@Autowired
	private SerializerType serializerType;
	@Autowired
	private CacheProperties cacheProperties;
	
	@Bean
	@ConditionalOnMissingBean
	public CacheManager cacheManager(RedisConnectionFactory factory) {
		log.info("配置CacheManager");
		final RedisCacheConfiguration config = RedisCacheConfiguration
			.defaultCacheConfig()
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(this.buildKeySerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.buildValueSerializer()))
			// 禁止缓存空值
//			.disableCachingNullValues()
			// 设置缓存前缀
			.prefixCacheNameWith(this.cachePrefix)
			// 设置过期时间
			.entryTtl(Duration.ofMinutes(this.cacheTtl));
		final Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();
		if(MapUtils.isNotEmpty(this.cacheProperties.getKeys())) {
			this.cacheProperties.getKeys().forEach((key, value) -> {
				log.info("配置缓存：{}-{}", key, value);
				cacheConfig.put(
					key,
					RedisCacheConfiguration
					.defaultCacheConfig()
					.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(this.buildKeySerializer()))
					.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.buildValueSerializer()))
//					.disableCachingNullValues()
					.prefixCacheNameWith(this.cachePrefix)
					.entryTtl(Duration.ofMinutes(value))
				);
			});
		}
		final RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
			.cacheDefaults(config)
			// 配置初始缓存
//			.initialCacheNames(null)
			// 不同缓存配置
			.withInitialCacheConfigurations(cacheConfig)
			.build();
		return cacheManager;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		template.setKeySerializer(this.buildKeySerializer());
		template.setValueSerializer(this.buildValueSerializer());
		template.afterPropertiesSet();
		return template;
	}
	
	@Bean
	@ConditionalOnBean(CacheManager.class)
	@ConditionalOnMissingBean
	public CacheService cacheService() {
		return new CacheServiceImpl();
	}
	
	/**
	 * @return 键序列化
	 */
	private RedisSerializer<String> buildKeySerializer() {
		return StringRedisSerializer.UTF_8;
	}
	
	/**
	 * @return 值序列化
	 */
	private RedisSerializer<?> buildValueSerializer() {
		if(this.serializerType == SerializerType.JACKSON) {
			final Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
			serializer.setObjectMapper(JSONUtils.buildSerializeMapper());
			return serializer;
		} else {
			return new JdkSerializationRedisSerializer();
		}
	}
	
}
