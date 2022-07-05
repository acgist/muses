package com.acgist.concurrent.distributed.lock;

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
	public String get(String key) {
		return this.redisTemplate.opsForValue().get(key);
	}

	@Override
	public boolean set(String key, String value, int ttl) {
		return this.redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(ttl));
	}

	@Override
	public void reset(String key, String value, int ttl) {
		this.redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(ttl));
	}

	@Override
	public void delete(String key, String value) {
		// TODO：严格意义来讲需要使用Lua脚本验证value
		this.redisTemplate.opsForValue().getOperations().delete(key);
	}

}
