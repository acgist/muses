package com.acgist.log.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 字段映射
 * 
 * @author acgist
 */
@Getter
@Setter
public class FieldMapping {

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 字段
	 */
	private String field;
	/**
	 * 数据库列
	 */
	private String column;
	/**
	 * 类型
	 */
	private Class<?> clazz;
	/**
	 * 枚举翻译
	 */
	private String transfer;

}
