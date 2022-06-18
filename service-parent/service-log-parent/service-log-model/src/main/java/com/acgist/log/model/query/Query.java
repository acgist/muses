package com.acgist.log.model.query;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询
 * 
 * @author acgist
 */
@Getter
@Setter
public class Query {

	/**
	 * 查询表
	 */
	private String[] table;
	/**
	 * 数据ID
	 */
	private Long[] id;
	/**
	 * 原始数据ID
	 */
	private Long[] sourceId;
	/**
	 * 关键字
	 */
	private String keywork;
	/**
	 * 分页页码
	 */
	private Integer page;
	/**
	 * 分页数量
	 */
	private Integer pageSize;
	
}
