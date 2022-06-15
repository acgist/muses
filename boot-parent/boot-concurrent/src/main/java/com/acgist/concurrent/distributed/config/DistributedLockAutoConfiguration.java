package com.acgist.concurrent.distributed.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.acgist.concurrent.distributed.lock.DistributedLock;
import com.acgist.concurrent.distributed.lock.RedisLock;

/**
 * 
 * 分布式锁
 * 
 * @author acgist
 */
@Configuration
@ConditionalOnClass(value = RedisTemplate.class)
public class DistributedLockAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DistributedLock redisLock() {
		return new RedisLock();
	}

}
