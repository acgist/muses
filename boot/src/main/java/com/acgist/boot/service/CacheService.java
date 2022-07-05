package com.acgist.boot.service;

/**
 * 缓存：延迟加载
 * 
 * @author acgist
 */
public interface CacheService {
	
	/**
	 * 存放缓存
	 * 
	 * @param name 缓存名称
	 * @param key 缓存键
	 * @param value 缓存值
	 * 
	 * @return 是否成功
	 */
	boolean cache(String name, String key, Object value);
	
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
	<T> T cache(String name, String key);
	
	/**
	 * 删除缓存
	 * 
	 * @param name 缓存名称
	 * @param key 缓存键
	 * 
	 * @return 是否成功
	 */
	boolean remove(String name, String key);
	
}
