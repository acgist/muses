package com.acgist.distributed.lock;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis分布式锁
 * 
 * @author acgist
 */
public class RedisLock implements DistributedLock {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public boolean set(String key, String value, int ttl) {
		return this.redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(ttl));
	}

	@Override
	public void reset(String key, String value, int ttl) {
		this.redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
	}

	@Override
	public String get(String key) {
		return this.redisTemplate.opsForValue().get(key);
	}

	@Override
	public void delete(String key) {
		this.redisTemplate.opsForValue().getOperations().delete(key);
	}

}
