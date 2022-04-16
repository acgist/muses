package com.acgist.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.acgist.boot.model.MessageCodeException;

/**
 * 缓存管理
 * 
 * @author acgist
 */
public class CacheService {

	@Autowired
	private CacheManager cacheManager;
	
	/**
	 * 存放缓存
	 * 
	 * @param name 缓存名称
	 * @param key 缓存键
	 * @param value 缓存值
	 * 
	 * @return 是否成功
	 */
	public boolean cache(String name, String key, Object value) {
		final Cache cache = this.cacheManager.getCache(name);
		if(cache == null) {
			throw MessageCodeException.of("缓存不存在：" + name);
		}
		cache.put(key, value);
		return true;
	}
	
	/**
	 * 获取缓存
	 * 
	 * @param <T> 缓存类型
	 * 
	 * @param name 缓存名称
	 * @param key 缓存键
	 * 
	 * @return 缓存值
	 */
	@SuppressWarnings("unchecked")
	public <T> T cahce(String name, String key) {
		final Cache cache = this.cacheManager.getCache(name);
		if(cache == null) {
			throw MessageCodeException.of("缓存不存在：" + name);
		}
		final ValueWrapper valueWrapper = cache.get(key);
		if(valueWrapper == null) {
			return null;
		}
		return (T) valueWrapper.get();
	}
	
	/**
	 * 删除缓存
	 * 
	 * @param name 缓存名称
	 * @param key 缓存键
	 * 
	 * @return 是否成功
	 */
	public boolean remove(String name, String key) {
		final Cache cache = this.cacheManager.getCache(name);
		if(cache == null) {
			throw MessageCodeException.of("缓存不存在：" + name);
		}
		cache.evict(key);
		return true;
	}
	
}
