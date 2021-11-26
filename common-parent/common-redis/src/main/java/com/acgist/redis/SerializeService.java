package com.acgist.redis;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.acgist.common.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SerializeService {

	public static final RedisSerializer<String> buildKeySerializer() {
		return StringRedisSerializer.UTF_8;
	}

	public static final RedisSerializer<?> buildValueSerializer() {
		final ObjectMapper mapper = JSONUtils.buildSerializeMapper();
		final Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		serializer.setObjectMapper(mapper);
		return serializer;
	}

}
