package com.acgist.service;

import java.util.HashMap;
import java.util.Map;

import com.acgist.boot.model.ModifyOptional;
import com.acgist.boot.service.CacheService;
import com.acgist.boot.utils.SpringUtils;

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
	 * 系统缓存
	 */
	ModifyOptional<CacheService> CACHE_SERVICE = ModifyOptional.of(() -> SpringUtils.getBeanNullable(CacheService.class));
	
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
	 * 
	 * @return 枚举数据（不能为空）
	 */
	default Map<String, String> transfer(String group) {
		Map<String, String> map = null;
		// 系统缓存
		final CacheService cacheService = TransferService.CACHE_SERVICE.build();
		if(cacheService != null) {
			map = cacheService.cache(TransferService.CACHE_TRANSFER, group);
			if(map != null) {
				return map;
			}
		}
		// 数据库查询
		map = this.select(group);
		if(map != null) {
			if(cacheService != null) {
				cacheService.cache(TransferService.CACHE_TRANSFER, group, map);
			}
			return map;
		}
		return Map.of();
	}

	/**
	 * 枚举翻译
	 * 
	 * @param groups 枚举分组
	 * 
	 * @return 枚举数据（不能为空）
	 */
	default Map<String, String> transfer(String [] groups) {
		final Map<String, String> map = new HashMap<>();
		for (String group : groups) {
			map.putAll(this.transfer(group));
		}
		return map;
	}
	
	/**
	 * 枚举翻译
	 * 
	 * @param group 枚举分组
	 * @param name 枚举名称
	 * 
	 * @return 枚举值
	 */
	default String transfer(String group, String name) {
		return this.transfer(group).getOrDefault(name, name);
	}
	
	/**
	 * 枚举翻译
	 * 
	 * @param groups 枚举分组
	 * @param name 枚举名称
	 * 
	 * @return 枚举值
	 */
	default String transfer(String[] groups, String name) {
		return this.transfer(groups).getOrDefault(name, name);
	}
	
}
