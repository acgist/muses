package com.acgist.log.model.message;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Kafka日志消息
 * 
 * @author acgist
 */
@Getter
@Setter
public class LogMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 操作类型
	 */
	private String type;
	/**
	 * 数据库表
	 */
	private String table;
	/**
	 * 数据库名称
	 */
	private String database;
	/**
	 * 是否是DDL
	 */
	private Boolean isDdl;
	/**
	 * 数据
	 */
	private List<Object> data;
	/**
	 * 差异数据
	 */
	private List<Object> old;
	
}
