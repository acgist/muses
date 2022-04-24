package com.acgist.service.excel;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.acgist.boot.utils.SpringUtils;
import com.acgist.service.BootExcelService.Formatter;
import com.acgist.service.CacheService;
import com.acgist.service.TransferService;

/**
 * 枚举格式化工具
 * 
 * @author acgist
 */
public class TransferFormatter implements Formatter {

	/**
	 * 枚举分组
	 */
	private final ThreadLocal<String> group = new ThreadLocal<>();
	/**
	 * 缓存
	 */
	private final CacheService cacheService;
	/**
	 * 枚举翻译服务
	 */
	private final TransferService transferService;
	
	public TransferFormatter() {
		this.cacheService = SpringUtils.getBeanNullable(CacheService.class);
		this.transferService = SpringUtils.getBeanNullable(TransferService.class);
	}
	
	/**
	 * @param group 枚举分组
	 */
	public void group(String group) {
		this.group.set(group);
	}

	@Override
	public String formatProxy(Object object) {
		final String key = object.toString();
		return this.transferMap().getOrDefault(key, key);
	}
	
	@Override
	public Object parseProxy(Object object) {
		return this.transferMap().entrySet().stream()
			.filter(entry -> Objects.equals(entry.getValue(), Objects.toString(object, null)))
			.map(Entry::getKey);
	}
	
	/**
	 * @return 分组翻译
	 */
	private Map<String, String> transferMap() {
		if(this.cacheService == null || this.transferService == null) {
			return Map.of();
		}
		final String groupName = this.group.get();
		Map<String, String> transferMap = this.cacheService.cache(CacheService.CACHE_TRANSFER, groupName);
		if(transferMap == null) {
			transferMap = this.transferService.select(groupName);
			this.cacheService.cache(CacheService.CACHE_TRANSFER, groupName, transferMap);
		}
		return transferMap;
	}
	
}
