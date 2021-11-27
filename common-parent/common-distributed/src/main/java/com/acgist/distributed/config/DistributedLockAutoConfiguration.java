package com.acgist.distributed.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.acgist.distributed.lock.DistributedLock;
import com.acgist.distributed.lock.RedisLock;

@Configuration
@ConditionalOnClass(value = RedisTemplate.class)
public class DistributedLockAutoConfiguration {

	@Bean
	@ConditionalOnClass(value = RedisTemplate.class)
	public DistributedLock redisLock() {
		return new RedisLock();
	}

}
