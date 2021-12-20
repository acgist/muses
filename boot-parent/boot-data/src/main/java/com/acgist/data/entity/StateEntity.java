package com.acgist.data.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 数据库实体类
 * 
 * @author acgist
 */
@MappedSuperclass
public abstract class StateEntity extends NameEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_SORTED = "sorted";
	public static final String PROPERTY_ENABLE = "enable";
	
	/**
	 * 排序
	 */
	@Column(columnDefinition = "int default 0")
	private Integer sorted;
	/**
	 * 是否可用
	 */
	@Column(columnDefinition = "bit default true")
	private Boolean enable;

	public Integer getSorted() {
		return sorted;
	}

	public void setSorted(Integer sorted) {
		this.sorted = sorted;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

}
