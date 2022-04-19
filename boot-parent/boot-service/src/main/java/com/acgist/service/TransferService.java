package com.acgist.service;

import java.util.Map;

/**
 * 枚举翻译服务
 * 
 * @author acgist
 */
public interface TransferService {

	/**
	 * 枚举查询
	 * 
	 * @param group 枚举分组
	 * 
	 * @return 枚举数据
	 */
	Map<String, String> select(String group);
	
}
