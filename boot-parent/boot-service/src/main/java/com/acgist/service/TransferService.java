package com.acgist.service;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 枚举翻译服务
 * 
 * @author acgist
 */
public interface TransferService {

	/**
	 * 枚举缓存
	 */
	String CACHE_TRANSFER = "transfer";
	
	/**
	 * 弱引用缓存
	 */
	Map<String, Map<String, String>> CACHE = new WeakHashMap<>();
	
	/**
	 * 枚举查询
	 * 
	 * @param group 枚举分组
	 * 
	 * @return 枚举数据（不能为空）
	 */
	Map<String, String> select(String group);
	
	/**
	 * 枚举翻译
	 * 
	 * @param group 枚举分组
	 * @param name 枚举名称
	 * 
	 * @return 枚举值
	 */
	default String select(String group, String name) {
		synchronized (CACHE) {
			Map<String, String> map = CACHE.get(group);
			if(map != null) {
				return map.getOrDefault(name, name);
			}
			map = this.select(group);
			if(map != null) {
				CACHE.put(group, map);
				return map.getOrDefault(name, name);
			}
			return name;
		}
	}
	
}
