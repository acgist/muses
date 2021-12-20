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
	public static final String PROPERTY_DELETED = "deleted";
	
	/**
	 * 排序
	 */
	@Column
	private Integer sorted;
	/**
	 * 是否可用
	 */
	@Column
	private Boolean enable;
	/**
	 * 是否删除
	 */
	@Column
	private Boolean deleted;

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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
