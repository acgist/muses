package com.acgist.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.acgist.boot.model.MessageCodeException;
import com.acgist.boot.service.CacheService;

/**
 * 缓存管理
 * 
 * @author acgist
 */
public class CacheServiceImpl implements CacheService {

	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public boolean cache(String name, String key, Object value) {
		final Cache cache = this.cacheManager.getCache(name);
		if(cache == null) {
			throw MessageCodeException.of("缓存不存在：", name);
		}
		cache.put(key, value);
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T cache(String name, String key) {
		final Cache cache = this.cacheManager.getCache(name);
		if(cache == null) {
			throw MessageCodeException.of("缓存不存在：", name);
		}
		final ValueWrapper valueWrapper = cache.get(key);
		if(valueWrapper == null) {
			return null;
		}
		return (T) valueWrapper.get();
	}
	
	@Override
	public boolean remove(String name, String key) {
		final Cache cache = this.cacheManager.getCache(name);
		if(cache == null) {
			throw MessageCodeException.of("缓存不存在：", name);
		}
		cache.evict(key);
		return true;
	}
	
}
