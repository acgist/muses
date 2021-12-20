package com.acgist.data.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 数据库实体类
 * 
 * @author acgist
 */
@MappedSuperclass
public abstract class NameEntity extends DataEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_MEMO = "memo";
	
	/**
	 * 名称
	 */
	@Column(length = 32, nullable = false)
	private String name;
	/**
	 * 描述
	 */
	@Column(length = 128)
	private String memo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
