package com.acgist.concurrent.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.acgist.concurrent.lock.DistributedLock;
import com.acgist.concurrent.lock.RedisLock;
import com.acgist.concurrent.service.ExecutorService;
import com.acgist.concurrent.service.impl.ExecutorServiceImpl;

/**
 * 高并发分布式自动配置
 * 
 * @author acgist
 */
@Configuration
@ConditionalOnClass(value = RedisTemplate.class)
public class ConcurrentAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DistributedLock redisLock() {
		return new RedisLock();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ExecutorService executorService() {
		return new ExecutorServiceImpl();
	}

}
