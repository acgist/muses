package com.acgist.data.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 排序
 * 
 * @author acgist
 */
@MappedSuperclass
public abstract class OrderEntity extends DataEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_SORT = "sort";
	
	/**
	 * 排序
	 */
	@Column
	private Short sort;

	public Short getSort() {
		return sort;
	}

	public void setSort(Short sort) {
		this.sort = sort;
	}
	
}
