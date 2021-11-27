package com.acgist.scheduled.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import com.acgist.scheduled.aop.DistributedScheduledConfiguration;
import com.acgist.scheduled.lock.DistributedLock;
import com.acgist.scheduled.lock.RedisLock;

@Import(DistributedScheduledConfiguration.class)
@Configuration
@ConditionalOnClass(value = RedisTemplate.class)
public class DistributedAutoConfiguration {

	@Bean
	@ConditionalOnClass(value = RedisTemplate.class)
	public DistributedLock redisLock() {
		return new RedisLock();
	}

}
