package com.acgist.log.service;

import com.acgist.log.model.message.LogMessage;

/**
 * 日志
 * 
 * @author acgist
 */
public interface LogService {

	/**
	 * 记录日志
	 * 
	 * @param logMessage 日志
	 */
	void log(LogMessage logMessage);

	/**
	 * 删除日志历史
	 * 
	 * @param table 表名
	 * @param sourceId 原始数据ID
	 */
	void deleteHistory(String table, Long sourceId);

}
