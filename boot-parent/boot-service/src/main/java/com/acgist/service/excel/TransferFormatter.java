package com.acgist.service.excel;

import java.util.Map;

import com.acgist.boot.SpringUtils;
import com.acgist.service.BootExcelService.Formatter;
import com.acgist.service.TransferService;
import com.acgist.service.impl.CacheService;

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
		this.cacheService = SpringUtils.getBean(CacheService.class);
		this.transferService = SpringUtils.getBean(TransferService.class);
	}
	
	/**
	 * @param group 枚举分组
	 */
	public void group(String group) {
		this.group.set(group);
	}

	@Override
	public Object parse(Object object) {
		if(object == null) {
			return Formatter.super.parse(object);
		}
		final String groupName = this.group.get();
		Map<String, String> transferMap = this.cacheService.cache(CacheService.CACHE_TRANSFER, groupName);
		if(transferMap == null) {
			final Map<String, String> map = this.transferService.select(groupName);
			transferMap = map == null ? Map.of() : map;
			this.cacheService.cache(CacheService.CACHE_TRANSFER, groupName, transferMap);
		}
		return transferMap.getOrDefault(object.toString(), object.toString());
	}
	
}
