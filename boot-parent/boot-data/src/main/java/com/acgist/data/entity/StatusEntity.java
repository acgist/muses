package com.acgist.data.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 状态
 * 
 * @author acgist
 */
@MappedSuperclass
public abstract class StatusEntity extends DataEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_ENABLE = "enable";
	public static final String PROPERTY_DELETE = "delete";
	
	/**
	 * 是否可用
	 */
	@Column
	private Boolean enable;
	/**
	 * 是否删除
	 */
	@Column
	private Boolean delete;

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

}
