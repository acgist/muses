package com.acgist.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库列
 * 
 * @author acgist
 */
@Getter
@Setter
public class TableColumnDto {

	/**
	 * Java字段
	 */
	private String field;
	/**
	 * 数据库列类型
	 */
	private String type;
	/**
	 * 数据库列
	 */
	private String column;
	/**
	 * 数据库列描述
	 */
	private String comment;
	
	/**
	 * 注解简单描述
	 * 
	 * @return 简单描述
	 */
	public String getSimpleComment() {
		return this.comment
			.replace("\r\n", " ")
			.replace('\r', ' ')
			.replace('\n', ' ')
			.replace("\"", "\\\"");
	}
	
}
