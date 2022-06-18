package com.acgist.model.dto;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库表
 * 
 * @author acgist
 */
@Getter
@Setter
public class TableDto {

	/**
	 * 数据库表
	 */
	private String table;
	/**
	 * 数据库表描述
	 */
	private String comment;
	/**
	 * 数据库列
	 */
	private List<TableColumnDto> columns;
	
	/**
	 * 获取数据库列
	 * 
	 * @param column 列名
	 * 
	 * @return 数据库列
	 */
	public TableColumnDto getColumn(String column) {
		return this.columns.stream()
			.filter(value -> Objects.equals(value.getColumn(), column))
			.findFirst()
			.orElse(null);
	}
	
}
