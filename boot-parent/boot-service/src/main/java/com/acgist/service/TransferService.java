package com.acgist.service;

import java.util.Map;

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
	 * 枚举查询
	 * 
	 * @param group 枚举分组
	 * 
	 * @return 枚举数据（不能为空）
	 */
	Map<String, String> select(String group);
	
}
