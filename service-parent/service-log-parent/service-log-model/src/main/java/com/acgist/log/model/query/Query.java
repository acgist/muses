package com.acgist.log.model.query;

import java.time.LocalDateTime;

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
	 * 数据ID
	 */
	private Long[] id;
	/**
	 * 查询表
	 */
	private String[] table;
	/**
	 * 原始数据ID
	 */
	private Long[] sourceId;
	/**
	 * 关键字
	 */
	private String keyword;
	/**
	 * 时间范围
	 */
	private LocalDateTime[] createDate;
	/**
	 * 分页页码
	 */
	private Integer page = 0;
	/**
	 * 分页数量
	 */
	private Integer pageSize = 20;
	
}
